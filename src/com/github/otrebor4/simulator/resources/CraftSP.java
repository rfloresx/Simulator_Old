package com.github.otrebor4.simulator.resources;


import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EnumGamemode;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NetHandler;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.PathEntity;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;



import com.github.otrebor4.simulator.Settings;
import com.github.otrebor4.simulator.SP.actinions.WorldManipulationAction;
import com.github.otrebor4.simulator.resources.Animator.Animation;
import com.github.otrebor4.simulator.util.Messaging;
import com.github.otrebor4.simulator.util.Timer;
import com.github.otrebor4.simulator.util.Vector3;

public class CraftSP extends EntityPlayer {
    public SimulatorNPC npc;
    private PathEntity path;
    protected Location dest;

    protected final Animator animations = new Animator(this);
    protected Entity targetEntity;
    protected boolean targetAggro = false;
    protected boolean autoPathToTarget = true;
    protected float pathingRange = 16;

    private boolean hasAttacked = false;
    private int pathTicks = 0;
    private int pathTickLimit = -1;
    private int stationaryTicks = 0;
    private int stationaryTickLimit = -1;
    private int prevX, prevY, prevZ;
    private static final double JUMP_FACTOR = 0.07D;
    protected int my_delay = 0;
    protected Vector3 oldDirection;
    Timer timer = new Timer();
    
    public CraftSP(MinecraftServer minecraftserver, World world, String s, ItemInWorldManager iteminworldmanager) {
        super(minecraftserver, world, s, iteminworldmanager);
        
        iteminworldmanager.setGameMode(EnumGamemode.SURVIVAL  );

        SimulatorSocket socket = new SimulatorSocket();
        NetworkManager netMgr = null;
		try {
			netMgr = new MyNetworkManager(socket, "npc mgr", new NetHandler() {
				@Override
				public boolean a() {

					return false;
				}
			}, server.E().getPrivate());
		} catch (IOException e) {	}
		
		if(netMgr != null){
			this.netServerHandler = new MyNetHandler(minecraftserver, this, netMgr);
        	netMgr.a(this.netServerHandler);
		}
        try {
            socket.close();
        } catch (IOException ex) {}
    }
    
    public void Update(){
    	
    	applyGravity();
    	applySoffocation();
    	
    	if (this.attackTicks > 0)
            --this.attackTicks;
        if (this.noDamageTicks > 0)
            --this.noDamageTicks; 
    }

    private void attackEntity(Entity entity) {
        this.attackTicks = 20;
        if (isHoldingBow() && distance(entity) >= Settings.getDouble("MinArrowRange")) {
            boolean up = this.random.nextBoolean();
            this.yaw += this.random.nextInt(5) * (up ? 1 : -1);

            up = this.random.nextBoolean();
            this.pitch += this.random.nextInt(5) * (up ? 1 : -1);
            
            this.getBukkitEntity().launchProjectile(Arrow.class);
        } else {
            this.performAction(Animation.SWING_ARM);
            LivingEntity other = (LivingEntity) entity.getBukkitEntity();
            other.damage(this.inventory.a(entity));
        }
        hasAttacked = true;

    }

    PathEntity createPathEntity(int x, int y, int z) {
        return this.world.a(this, x, y, z, pathingRange, true, false, false, true);
    }

    private double distance(Entity entity) {
        return entity.getBukkitEntity().getLocation().distance(this.getBukkitEntity().getLocation());
    }

    protected EntityHuman getClosestPlayer(double range) {
        EntityHuman entityhuman = this.world.findNearbyPlayer(this, range);
        return entityhuman != null && isInSight(entityhuman) ? entityhuman : null;
    }

    private Vec3D getPathVector() {
        Vec3D vec3d = path.a(this);
        double length = (this.width * 2.0F);
        while (vec3d != null && vec3d.d(this.locX, vec3d.b, this.locZ) < length * length) {
            this.path.a();
            if (this.path.b()) {
                vec3d = null;
                cancelPath();
            } else {
                vec3d = this.path.a(this);
            }
        }
        return vec3d;
    }

    public int getStationaryTicks() {
        return this.stationaryTicks;
    }

    private float getYawDifference(double diffZ, double diffX) {
        float vectorYaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
        float diffYaw = vectorYaw - this.yaw;

	    this.bs = this.bw;
	    
        while (diffYaw >= 180.0F) {
            diffYaw -= 360.0F;
        }
        if (diffYaw > 30.0F) {
            diffYaw = 30.0F;
        }
        if (diffYaw < -30.0F) {
            diffYaw = -30.0F;
        }
        return diffYaw;
    }

    private void handleMove(Vec3D vector) {
        int yHeight = MathHelper.floor(this.boundingBox.b + 0.5D);
        boolean inWater = this.getBukkitEntity().getRemainingAir() < 20;
        boolean onFire = this.getBukkitEntity().getFireTicks() > 0;
        if (vector != null) {
            double diffX = vector.a - this.locX;
            double diffZ = vector.c - this.locZ;
            double diffY = vector.b - yHeight;
            float diffYaw = getYawDifference(diffZ, diffX);

            this.yaw += diffYaw;
            this.as += diffYaw;
            if (diffY > 0.0D) {
                jump();
            }

            // Walk.
            if(this.isSprinting()){ this.bs = 1.6f; }
            else{ this.bs = 7.0f;}
            
            this.e(this.br, this.bs);
        }
        if (this.positionChanged && !this.pathFinished()) {
            jump();
        }
        if (this.random.nextFloat() < 0.8F && (inWater || onFire)) {
            this.motY += 0.04D;
        }
    }
    
    public void applyGravity() {
    	org.bukkit.World world = this.getBukkitEntity().getWorld();
    	Block block = world.getBlockAt(this.getBukkitEntity().getLocation());
    	if(block.getRelative(BlockFace.DOWN).getType() == Material.AIR){
    		this.onGround = false;
    	}
    	this.e(this.br, 0);
    }
    
    public void applySoffocation(){
    	if(!this.isAlive()){
    		return;
    	}
    	org.bukkit.World world = this.getBukkitEntity().getWorld();
    	Block block = world.getBlockAt(this.getBukkitEntity().getLocation());
    	if( !block.getRelative(BlockFace.UP).isEmpty() ){ //|| !block.isEmpty()){
    		if( this.getAirTicks() > 0 ){
    			if(timer.getTimeSecons() > .1f){
    				Messaging.log("air " + this.getAirTicks() + " / " + this.maxAirTicks);
    				if( this.getAirTicks() > 0){
    					this.setAirTicks( this.getAirTicks() - this.maxAirTicks/10 );
    				}
    				timer.start();
    			}
    		}
    	}else{
    		timer.start();
    		this.setAirTicks(this.maxAirTicks);
    	}
    	if(this.getAirTicks() <= 0){
    		if(timer.getTimeSecons() > .5f){
    			Messaging.log("Health " + this.getHealth() + " / " + this.getMaxHealth());
    			this.setHealth( this.getHealth() - 2);
    			this.performAction(Animation.ACT_HURT);
    			timer.start();
    		}
    		if(this.getHealth() <= 0){
    			this.die(DamageSource.DROWN);
    			//this.deathEvent();
    			Messaging.log("died by soffocation");
    		}
    	}
    }

    private boolean isHoldingBow() {
        return getBukkitEntity().getItemInHand() != null && getBukkitEntity().getItemInHand().getType() == Material.BOW;
    }

    public boolean isInSight(Entity entity) {
        return this.l(entity);
    }

    private boolean isWithinAttackRange(Entity entity, double distance) {
        // Distance from EntityCreature.
        return this.attackTicks <= 0
                && ((isHoldingBow() && (distance > Settings.getDouble("MinArrowRange") && distance < Settings
                        .getDouble("MaxArrowRange"))) || (distance < 1.5F
                        && entity.boundingBox.e > this.boundingBox.b && entity.boundingBox.b < this.boundingBox.e)
                        && isInSight(entity));
    }

    protected void jump() {
        if (this.onGround) {
            this.motY = 0.42D + JUMP_FACTOR;
        }
    }
    
    protected void Swim(){
    	//TODO:need fix.
    	org.bukkit.World world = this.getBukkitEntity().getWorld();
    	Block block = world.getBlockAt(this.getBukkitEntity().getLocation());
    	if(block.getRelative(BlockFace.UP).isLiquid() || block.isLiquid() ){
    		this.motY = 0.42D;
    	}
    }
    
    public void moveTick() {
        if (this.dead) {
            if (this.targetEntity != null || this.path != null)
                cancelTarget();
            return;
        }
        hasAttacked = false;
      
        takeRandomPath();
        
        updateTarget();
        if (this.path != null || this.targetEntity != null) {
            updatePathingState();
        }
        if (this.path != null) {
            Vec3D vector = getPathVector();
            if (vector != null) {
                handleMove(vector);
            }
        }
    }
   
    public void MoveToDirection(int x, int y){
    	if(oldDirection == null)
    		oldDirection = new Vector3(x,0,y);
    	 if (this.dead) {
             if (this.targetEntity != null || this.path != null)
                 cancelTarget();
             return;
         }
         hasAttacked = false;

        if(this.pathFinished() || (oldDirection.x != x || oldDirection.y != y) ){
        	oldDirection = new Vector3(x,0,y);
        	updatePathTo(x,y);
    	}
         
         if (this.path != null || this.targetEntity != null) {
             updatePathingState();
         }
         if (this.path != null) {
             Vec3D vector = getPathVector();
             if (vector != null) {
                 handleMove(vector);
             }
         }	
    }
    
    public boolean MoveToPos(int x, int y,  int z){
    	if(oldDirection == null)
    		oldDirection = new Vector3(x,0,y);
    	
    	if(oldDirection.x != x || oldDirection.y != y || oldDirection.z != z ){
        	oldDirection = new Vector3(x,y,z);
        	setPathTo(x, y, z );
    	}
    	
    	if (this.dead) {
             if (this.targetEntity != null || this.path != null)
                 cancelTarget();
             return true;
        }
    	
        if(pathFinished()){
        	return true;
        }
         
        if (this.path != null) {
        	updatePathingState();
            Vec3D vector = getPathVector();
            if (vector != null) {
                handleMove(vector);
            }
        }
        
        return false;
        
    }
    
    public boolean pathFinished() {
        return path == null;
    }

    public void performAction(Animation action) {
        this.animations.performAnimation(action);
    }

    public void cancelPath() {
        this.path = null;
        this.dest = null;
        this.pathTicks = this.stationaryTicks = 0;
        this.pathTickLimit = this.stationaryTickLimit = -1;
        this.pathingRange = 16;
    }

    public void cancelTarget() {
        this.targetEntity = null;
        this.targetAggro = false;
        cancelPath();
    }

    public void setTarget(LivingEntity entity, boolean aggro, int maxTicks, int maxStationaryTicks, double range) {
        this.targetEntity = ((CraftLivingEntity) entity).getHandle();
        this.targetAggro = aggro;
        this.pathTickLimit = maxTicks;
        this.pathingRange = (float) range;
        this.stationaryTickLimit = maxStationaryTicks;
    }

    public boolean startPath(Location loc, int maxTicks, int maxStationaryTicks, double range) {
        this.pathTickLimit = maxTicks;
        this.stationaryTickLimit = maxStationaryTicks;
        this.pathingRange = (float) range;

        if (loc != null) {
            this.path = createPathEntity(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            this.dest = loc.clone();
        }
        return pathFinished();
    }

    public boolean isaPlayerInRange(double range){
    	if(this.world.findNearbyPlayer(this, range) != null)
    		return true;
    	return false;
    }
    
    public boolean targetClosestPlayer(boolean aggro, double range) {
        this.targetEntity = this.getClosestPlayer(range);
        this.targetAggro = aggro;
        if(this.targetEntity != null)
        	return true;
        return false;
    }

    private void updatePathingState() {
        Location loc = this.bukkitEntity.getLocation();
        if (prevX == loc.getBlockX() && prevY == loc.getBlockY() && prevZ == loc.getBlockZ()) {
            ++stationaryTicks;
        } else {
            stationaryTicks = 0;
        }
        ++pathTicks;
        if ((pathTickLimit != -1 && pathTicks >= pathTickLimit)
                || (stationaryTickLimit != -1 && stationaryTicks >= stationaryTickLimit)) {
            cancelPath();
        }
        prevX = loc.getBlockX();
        prevY = loc.getBlockY();
        prevZ = loc.getBlockZ();
    }

    private void updateTarget() {
        	
        if (!this.hasAttacked && this.targetEntity != null && autoPathToTarget) {
            this.path = this.world.findPath(this, this.targetEntity, pathingRange, true, false, false, true);
            this.dest = this.targetEntity.getBukkitEntity().getLocation();
        }
        if (targetEntity == null){
        	return;
        }
        if (this.targetEntity.dead || !targetEntity.world.equals(this.world)) {
            cancelTarget();
            return;
        }
        
        if( distance(this.targetEntity) > 7){
        	this.cancelTarget();
        	return;
        }
        
       // NPCManager.faceEntity(this.npc, targetEntity.getBukkitEntity());
        if (!targetAggro)
            return;
        if (isWithinAttackRange(this.targetEntity, distance(this.targetEntity))) {
            this.attackEntity(this.targetEntity);
        }
    }
    
    private void takeRandomPath(){
    	if( this.hasAttacked || this.targetEntity != null )
    		return;
    	Location loc = this.bukkitEntity.getLocation();
    	int randx = (random.nextInt() % 32) - 16 + loc.getBlockX();
    	int randy = (random.nextInt() % 32) - 16 + loc.getBlockY();
    	int randz = (random.nextInt() % 32) - 16 + loc.getBlockZ();
    
    	this.path = createPathEntity( randx, randy, randz );
    	this.dest = new Location( this.world.getWorld(), randx, randy, randz );
    
    }
    
    public void setRun(boolean val){
    	this.setSprinting( val);
    	this.getBukkitEntity().setSprinting(val);
    }
    
    //update path to a direction;
    private void updatePathTo( int x, int y){
    	
    	Location loc = this.bukkitEntity.getLocation();
    	
    	int randx = x * random.nextInt(16) + loc.getBlockX();
    	int randy = ((random.nextInt(32)) - 16) + loc.getBlockY();
    	int randz = y * random.nextInt(16) + loc.getBlockZ();
    	
    	if( x == 0){
    		randx = (random.nextInt(32) - 16) + loc.getBlockX();
    	}
    	if( y == 0){
    		randz = (random.nextInt(32) - 16) + loc.getBlockZ();
    	}
    	
    	//PathEntity randPath = createPathEntity( randx, randy, randz );
    	this.path = createPathEntity( randx, randy, randz ); //this.world.findPath(this, randPath, 16, true, false, false, true);
    	 
    	this.dest = new Location( this.world.getWorld(), randx, randy, randz );
    	
    }
    
    private void setPathTo(int x, int y, int z){
    	this.path = createPathEntity( x, y, z );
    	this.dest = new Location( this.world.getWorld(), x, y, z );
    }
    
    public static List<Monster> getMonsterIn( CraftSP npc, float range ){
		Collection<Monster> list = npc.getBukkitEntity().getWorld().getEntitiesByClass( Monster.class );
		List<Monster> m_list = new LinkedList<Monster>();
		Location pos = npc.getBukkitEntity().getLocation();
		
		for( Monster m : list){
			if(m.getLocation().distance( pos) <range){
				m_list.add(m);
			}
		}
		
		return m_list;
	}
    
    public Location getLocation(){
    	return this.getBukkitEntity().getLocation();
    }

    public void faceTo( Location pl) {
    	
        if (npc.getWorld() !=  pl.getWorld())
            return;
        
        Location loc = npc.getLocation();
        double xDiff = pl.getX() - loc.getX();
        double yDiff = pl.getY() - loc.getY();
        double zDiff = pl.getZ() - loc.getZ();
        double DistanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        double DistanceY = Math.sqrt(DistanceXZ * DistanceXZ + yDiff * yDiff);
        double yaw = (Math.acos(xDiff / DistanceXZ) * 180 / Math.PI);
        double pitch = (Math.acos(yDiff / DistanceY) * 180 / Math.PI) - 90;
        if (zDiff < 0.0) {
            yaw = yaw + (Math.abs(180 - yaw) * 2);
        }
        npc.getHandle().yaw = (float) yaw - 90;
        npc.getHandle().as = npc.getHandle().yaw;
        npc.getHandle().pitch = (float) pitch;
    }
    
    public boolean IsFacingT( Location l){
    	if (npc.getWorld() !=  l.getWorld())
            return false;
    	
    	Location loc = npc.getLocation();
        double xDiff = l.getX() - loc.getX();
        double yDiff = l.getY() - loc.getY();
        double zDiff = l.getZ() - loc.getZ();
        double DistanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        double DistanceY = Math.sqrt(DistanceXZ * DistanceXZ + yDiff * yDiff);
        double yaw = (Math.acos(xDiff / DistanceXZ) * 180 / Math.PI);
        double pitch = (Math.acos(yDiff / DistanceY) * 180 / Math.PI) - 90;
        if (zDiff < 0.0) {
            yaw = yaw + (Math.abs(180 - yaw) * 2);
        }
        yaw -= 90;
        boolean byaw = false, bpitch = false;
       
        if(npc.getHandle().yaw <= yaw + 10 && npc.getHandle().yaw >= yaw - 10 ){
        	byaw = true;
        }
        if( npc.getHandle().pitch <= pitch + 10 && npc.getHandle().pitch >= pitch - 10 ){
        	bpitch = true;
        }
        return byaw && bpitch;
    }

	public boolean isInSight(CraftEntity entity) {
		if(entity.getHandle() instanceof Entity)
			return this.l(entity.getHandle());
		return false;
	}
	
	public boolean isInSight(Block block){
		if( block.getLocation().getWorld() != this.getBukkitEntity().getLocation().getWorld())
			return false;
		Block front = WorldManipulationAction.getFirstBlockInDirection( this.getBukkitEntity().getLocation(),  block.getLocation());
		if(front == null ){
			return false;
		}
		
		return inSameLoc(  front.getLocation(), block.getLocation() );
	}
	
	public boolean canSeeLoc( Location loc){
		if( loc.getWorld() != this.getBukkitEntity().getLocation().getWorld())
			return false;
		Block block = WorldManipulationAction.getFirstBlockInDirection(this.getBukkitEntity().getLocation(), loc);
		if(block.isEmpty())
			return true;
		
		return block.getLocation() == loc;
	}

	public boolean inSameLoc( Location loc1, Location loc2){
		if(loc1 == null || loc2 == null)
			return false;
		
		if(loc1.getWorld() == loc2.getWorld()){
			boolean flag = (loc1.getBlockX() == loc2.getBlockX() &&
					loc1.getBlockY() == loc2.getBlockY() &&
					loc1.getBlockZ() == loc2.getBlockZ() );
			
			return flag;
		}
		return false;
	}
	
	public LivingEntity getTarget() {
        return this.targetEntity == null ? null : ((LivingEntity) this.targetEntity.getBukkitEntity());
    }

    public boolean hasTarget() {
        return this.targetEntity != null;
    }
}
