package nframe.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import nframe.NFIData;
import nframe.NFIObject;
import nframe.NFIProperty;
import nframe.NFIPropertyHandler;
import nframe.NFIPropertyManager;
import nframe.NFGUID;
import nframe.NFObject;

public class Base {
	public static final NFGUID usrOid = new NFGUID(0,1);
	public static final NFGUID plyOid = new NFGUID(0,2);
	public static final NFGUID equOid = new NFGUID(0,3);
	
	private static final ThreadLocal<Map<NFGUID, NFIObject>> objectContext;
	static{
		objectContext = new ThreadLocal<Map<NFGUID, NFIObject>>();
	}
	
	private static Map<NFGUID, NFIObject> getThreadLocalParam(){
		Map<NFGUID, NFIObject> paramInThreadLocal = objectContext.get();
		if (paramInThreadLocal == null){
			paramInThreadLocal = new HashMap<NFGUID, NFIObject>();
			objectContext.set(paramInThreadLocal);
		}
		return paramInThreadLocal;
	}
	
	private static void put(NFGUID oid, NFIObject obj){
		getThreadLocalParam().put(oid, obj);
	}
	
	private static NFIObject get(NFGUID oid){
		return getThreadLocalParam().get(oid);
	}
	
	private static final ThreadLocal<DBUser> cacheUser;
	static{
		cacheUser = new ThreadLocal<DBUser>();
	}
	
	private static final DBUser getCacheUser(){
		return cacheUser.get();
	}
	
	private static final void setCache(DBUser dbUser){
		cacheUser.set(dbUser);
	}
	
	private static final ThreadLocal<DBPlayer> cachePlayer;
	static{
		cachePlayer = new ThreadLocal<DBPlayer>();
	}
	
	private static final DBPlayer getCachePlayer(){
		return cachePlayer.get();
	}
	
	private static final void setCache(DBPlayer dbPly){
		cachePlayer.set(dbPly);
	}
	
	private static final ThreadLocal<List<DBEquip>> cacheAllEquips;
	static{
		cacheAllEquips = new ThreadLocal<List<DBEquip>>();
	}
	
	private static final List<DBEquip> getCacheAllEquips(){
		return cacheAllEquips.get();
	}
	
	private static final void setCache(List<DBEquip> equips){
		cacheAllEquips.set(equips);
	}
	
	private static final ThreadLocal<NetDown> cacheNetDown;
	static{
		cacheNetDown = new ThreadLocal<NetDown>();
	}
	
	private static final void setNetDown(NetDown down){
		cacheNetDown.set(down);
	}
	
	private static final NetDown getNetDown(){
		return cacheNetDown.get();
	}
	
	private static final void clearCache(){
		cacheUser.remove();
		cachePlayer.remove();
		cacheAllEquips.remove();
		cacheNetDown.remove();
	}
	
	class DBUser {
		public long uid; // primary 
		public String username = "";
		public String password = "";
		public byte limit;
	}
	
	class DBPlayer {
		public long uid; // primary 
		public String nickname;
		public long exp; // 玩家的经验值，完成任务会增长经验值
		public long coin; // 游戏内金币
		public long money; // 充值后的代币
		public long charge; // 累计充值
		public int level; // 玩家等级，经验值积累越多，等级越高
		public int vip; // vip等级，累计充值越多，vip等级越高
		public int quest; // 任务进度，线性增长，1开始，每个数字表示一个任务完成；完成任务会奖励玩家金币和经验
		public String title = ""; // 称号，和vip等级相关，越高vip，称号越好
	}
	
	class DBEquip {
		public long id; // primary 唯一实例id
		public long uid; // index
		public byte type; // 装备的类型，0~N，每个数字代表一种不同的装备
		public int level; // 装备等级
		public long exp; // 装备经验值，经验值积累越多，装备等级越高；消耗玩家金币转化为装备经验
		public long star; // 装备星级，必须花费代币才能升级，升级星级也会增加玩家经验值
	}
	
	enum ErrorCode {
		OK, // 成功
		USER_NOT_FOUND, // 用户未找到
		EQUIP_NOT_FOUND, // 装备未找到
		OUT_OF_LIMIT, // 超出限制
		VERIFY_FAILED, // 验证失败
		PWD_INVALID, // 密码不合法 
		COIN_NOT_ENOUGH, // 金币不足
		MONEY_NOT_ENOUGH, // 代币不足
		SERVER_EXCEPTION, // 服务器异常
	}
	
	/**
	 * @author Xiong
	 * 下行消息父类
	 */
	class NetDown {
		/** 操作结果状态码, 0成功，非0失败，代表错误代码*/
		private ErrorCode errc = ErrorCode.OK;
		/** 通用字段 */
		private long uid = -1;
		/** 发生变化的属性 */
		private Map<String, String> changedProps = new Hashtable<String, String>();
		
		public NetDown(){
			setNetDown(this);
		}
		
		public void addProp(String key, String value){
			changedProps.put(key, value);
		}
		
		public String getProp(String key){
			return changedProps.get(key);
		}
		
		public Map<String, String> getProps(){
			return changedProps;
		}

		public ErrorCode getErrc() {
			return errc;
		}

		public void setErrc(ErrorCode errc) {
			this.errc = errc;
		}

		public long getUid() {
			return uid;
		}

		public void setUid(long uid) {
			this.uid = uid;
		}
	}
	
	class NetDownLogin extends NetDown {
		private byte limit;
		private DBPlayer dbPly;
		private List<DBEquip> dbAllEquips;
		public byte getLimit() {
			return limit;
		}
		public void setLimit(byte limit) {
			this.limit = limit;
		}
		public DBPlayer getDbPly() {
			return dbPly;
		}
		public void setDbPly(DBPlayer dbPly) {
			this.dbPly = dbPly;
		}
		public List<DBEquip> getDbAllEquips() {
			return dbAllEquips;
		}
		public void setDbAllEquips(List<DBEquip> dbAllEquips) {
			this.dbAllEquips = dbAllEquips;
		}
	}
	
	class NetDownChangePwd extends NetDown {
	}
	
	class NetDownCompleteQuest extends NetDown {
	}
	
	class NetDownCharge extends NetDown {
	}
	
	class NetDownUpgradeEquip extends NetDown {
	}
	
	class NetDownUpgradeEquipStar extends NetDown {
	}
	
	
	/**
	 * 模拟数据库用户表
	 * username为key
	 */
	private static final Map<String, DBUser> tabUser = new ConcurrentHashMap<String, DBUser>();
	/**
	 * 模拟数据库玩家表
	 * uid为key
	 */
	private static final Map<Long, DBPlayer> tabPlayer = new ConcurrentHashMap<Long, DBPlayer>();
	/**
	 * 模拟数据库装备表
	 * uid为key，每个玩家都有一个组装备
	 */
	private static final Map<Long, List<DBEquip>> tabEquip = new ConcurrentHashMap<Long, List<DBEquip>>();
	
	/** 用户唯一id产生基础 */
	private static AtomicLong uidBase = new AtomicLong(0);
	/** 装备唯一id产生基础 */
	private static AtomicLong eidBase = new AtomicLong(0);

	/**
	 * @author Xiong
	 * 允许改密码次数限制
	 */
	public class LimitHandler implements NFIPropertyHandler {
		private Base cb;
		public LimitHandler(Base cb){
			this.cb = cb;
		}
		@Override
		public void handle(NFGUID oid, String propName, NFIData oldVar, NFIData newVar){
			cb.handleLimit(oid, propName, oldVar, newVar);
		}
	}
	
	public class LevelUpHandler implements NFIPropertyHandler {
		private Base cb;
		public LevelUpHandler(Base cb){
			this.cb = cb;
		}
		@Override
		public void handle(NFGUID oid, String propName, NFIData oldVar, NFIData newVar){
			cb.handleLevelUp(oid, propName, oldVar, newVar);
		}
	}
	
	public class VipLevelUpHandler implements NFIPropertyHandler {
		private Base cb;
		public VipLevelUpHandler(Base cb){
			this.cb = cb;
		}
		@Override
		public void handle(NFGUID oid, String propName, NFIData oldVar, NFIData newVar){
			cb.handleVipLevelUp(oid, propName, oldVar, newVar);
		}
	}
	
	public class EquipLevelUpHandler implements NFIPropertyHandler {
		private Base cb;
		public EquipLevelUpHandler(Base cb){
			this.cb = cb;
		}
		@Override
		public void handle(NFGUID oid, String propName, NFIData oldVar, NFIData newVar){
			cb.handleEquipLevelUp(oid, propName, oldVar, newVar);
		}
	}
	
	/**
	 * @author Xiong
	 * 特权相关（例如称号变得更霸气23333）
	 */
	public class PrivilegeHandler implements NFIPropertyHandler {
		private Base cb;
		public PrivilegeHandler(Base cb){
			this.cb = cb;
		}
		@Override
		public void handle(NFGUID oid, String propName, NFIData oldVar, NFIData newVar){
			cb.handlePrivilege(oid, propName, oldVar, newVar);
		}
	}
	
	public class QuestRewardHandler implements NFIPropertyHandler {
		private Base cb;
		public QuestRewardHandler(Base cb){
			this.cb = cb;
		}
		@Override
		public void handle(NFGUID oid, String propName, NFIData oldVar, NFIData newVar){
			cb.handleQuestReward(oid, propName, oldVar, newVar);
		}
	}
	
	@Test
	public void test() {
		final Base base = this;
		
		// 模拟多个线程并行处理客户端的请求
		int threadNum = Runtime.getRuntime().availableProcessors();
		List<Thread> thrs = new ArrayList<Thread>(threadNum);
		for (int i=0; i<threadNum; ++i){
			final int index = i;
			thrs.add(new Thread(){
				@Override
				public void run(){
					base.handleTest(index);
				}
			});
			thrs.get(i).start();
		}
		
		for (Thread thr : thrs){
			try{
				thr.join();
			}catch (InterruptedException e){
				System.out.println(e.getMessage());
			}
		}
		
		System.out.println("done.");
	}
	
	@SuppressWarnings("unused")
	public void handleTest(int index){
		/**
		 * 模拟处理几个客户端的请求
		 */
		
		// 模拟服务器启动时，每个处理线程进行的初始化
		init();
		
		String user1 = "user1" + index;
		String user2 = "user2" + index;
		
		// 用户1登录
		NetDownLogin downLogin1 = login(user1, "pwd");
		assertTrue(downLogin1.getErrc() == ErrorCode.OK);
		DBPlayer dbPly1 = downLogin1.getDbPly();
		long exp1 = dbPly1.exp;
		long coin1 = dbPly1.coin;
		long money1 = dbPly1.money;
		long charge1 = dbPly1.charge;
		int level1 = dbPly1.level;
		int vip1 = dbPly1.vip;
		int quest1 = dbPly1.quest;
		String title1 = dbPly1.title;
		assertTrue(coin1 == initCoin);
		List<DBEquip> dbEquips1 = downLogin1.getDbAllEquips();
		assertTrue(dbEquips1.size() == initEquipTypes.size());
		
		// 用户2登录
		NetDownLogin downLogin2 = login(user2, "pwd");
		assertTrue(downLogin2.getErrc() == ErrorCode.OK);
		DBPlayer dbPly2 = downLogin2.getDbPly();
		long exp2 = dbPly2.exp;
		long coin2 = dbPly2.coin;
		long money2 = dbPly2.money;
		long charge2 = dbPly2.charge;
		int level2 = dbPly2.level;
		int vip2 = dbPly2.vip;
		int quest2 = dbPly2.quest;
		String title2 = dbPly2.title;
		assertTrue(coin2 == initCoin);
		List<DBEquip> dbEquips2 = downLogin2.getDbAllEquips();
		assertTrue(dbEquips2.size() == initEquipTypes.size());
		
		// 用户1修改密码
		String tmpPwd1 = "pwd";
		for (int i=0; i<3; ++i){
			// 改3次，不会失败
			String newPwd = tmpPwd1 + "1";
			NetDownChangePwd down = changePassword(user1, tmpPwd1, newPwd);
			assertTrue(down.getErrc() == ErrorCode.OK);
			tmpPwd1 = newPwd;
		}
		// 第4次修改失败，超过修改限制
		assertTrue(changePassword(user1, tmpPwd1, tmpPwd1+"1").getErrc() == ErrorCode.OUT_OF_LIMIT);
		
		// 用户1用新密码登录
		downLogin1 = login(user1, "pwd111");
		assertTrue(downLogin1.getErrc() == ErrorCode.OK);
		
		// 用户2完成2个任务
		String tmpProp2 = null;
		for (int i=0; i<2; ++i){
			NetDownCompleteQuest down = completeQuest(downLogin2.getUid());
			assertTrue(down.getErrc() == ErrorCode.OK);
			exp2 += questExp;
			coin2 += questCoin;
			quest2++;
			assertTrue(Long.parseLong(down.getProp("player.exp")) == exp2);
			assertTrue(Long.parseLong(down.getProp("player.coin")) == coin2);
			assertTrue(Integer.parseInt(down.getProp("player.quest")) == quest2);
			tmpProp2 = down.getProp("player.level");
		}
		level2++;
		assertTrue(Integer.parseInt(tmpProp2) == level2);
		
		// 用户1充值60代币
		NetDownCharge downCharge1 = charge(downLogin1.getUid(), 60);
		assertTrue(downCharge1.getErrc() == ErrorCode.OK);
		charge1 += 60;
		money1 += 60;
		vip1++;
		title1 = "小康";
		assertTrue(Long.parseLong(downCharge1.getProp("player.charge")) == charge1);
		assertTrue(Long.parseLong(downCharge1.getProp("player.money")) == money1);
		assertTrue(Integer.parseInt(downCharge1.getProp("player.vip")) == vip1);
		assertTrue(downCharge1.getProp("player.title").equals(title1));
		
		// 保存用户2的装备0的数据
		DBEquip dbEquip02 = dbEquips2.get(0);
		long equExp02 = dbEquip02.exp;
		int equLevel02 = dbEquip02.level;
		long equStar02 = dbEquip02.star;
		
		// 用户2花费consumeCoin2金币强化装备0
		long consumeCoin2 = 35;
		NetDownUpgradeEquip downUpEquip2 = upgradeEquip(downLogin2.getUid(), dbEquip02.id, consumeCoin2);
		assertTrue(downUpEquip2.getErrc() == ErrorCode.OK);
		coin2 -= consumeCoin2;
		equExp02 += consumeCoin2;
		equLevel02 += 3;
		assertTrue(Long.parseLong(downUpEquip2.getProp("player.coin")) == coin2);
		assertTrue(Long.parseLong(downUpEquip2.getProp("equip.exp#"+dbEquip02.id)) == equExp02);
		assertTrue(Integer.parseInt(downUpEquip2.getProp("equip.level#"+dbEquip02.id)) == equLevel02);
		
		// 保存用户1的装备0的数据
		DBEquip dbEquip01 = dbEquips1.get(0);
		long equExp01 = dbEquip01.exp;
		int equLevel01 = dbEquip01.level;
		long equStar01 = dbEquip01.star;
		
		// 用户1花费consumeCoin1金币强化装备0
		long consumeCoin1 = 85;
		NetDownUpgradeEquip downUpEquip1 = upgradeEquip(downLogin1.getUid(), dbEquip01.id, consumeCoin1);
		assertTrue(downUpEquip1.getErrc() == ErrorCode.OK);
		coin1 -= consumeCoin1;
		equExp01 += consumeCoin1;
		equLevel01 += 8;
		assertTrue(Long.parseLong(downUpEquip1.getProp("player.coin")) == coin1);
		assertTrue(Long.parseLong(downUpEquip1.getProp("equip.exp#"+dbEquip01.id)) == equExp01);
		assertTrue(Integer.parseInt(downUpEquip1.getProp("equip.level#"+dbEquip01.id)) == equLevel01);
		
		// 用户1花费consumeMoney1代币升星装备0，但金币不够，返回失败
		long consumeMoney1 = 25;
		assertTrue(upgradeEquipStar(downLogin1.getUid(), dbEquip01.id, consumeMoney1).getErrc() == ErrorCode.COIN_NOT_ENOUGH);
		
		// 用户1完成1个任务
		NetDownCompleteQuest downCompleteQuest1 = completeQuest(downLogin1.getUid());
		assertTrue(downCompleteQuest1.getErrc() == ErrorCode.OK);
		exp1 += questExp;
		coin1 += questCoin;
		assertTrue(Long.parseLong(downCompleteQuest1.getProp("player.exp")) == exp1);
		assertTrue(Long.parseLong(downCompleteQuest1.getProp("player.coin")) == coin1);
		
		// 用户2完成1个任务
		NetDownCompleteQuest downCompleteQuest2 = completeQuest(downLogin2.getUid());
		assertTrue(downCompleteQuest2.getErrc() == ErrorCode.OK);
		exp2 += questExp;
		coin2 += questCoin;
		level2++;
		assertTrue(Long.parseLong(downCompleteQuest2.getProp("player.exp")) == exp2);
		assertTrue(Long.parseLong(downCompleteQuest2.getProp("player.coin")) == coin2);
		assertTrue(Integer.parseInt(downCompleteQuest2.getProp("player.level")) == level2);
		
		// 用户1再次花费consumeMoney1代币升星装备0
		NetDownUpgradeEquipStar downUpEquipStar1 = upgradeEquipStar(downLogin1.getUid(), dbEquip01.id, consumeMoney1);
		assertTrue(downUpEquipStar1.getErrc() == ErrorCode.OK);
		money1 -= consumeMoney1;
		coin1 -= consumeMoney1;
		equStar01 += consumeMoney1;
		exp1 += consumeMoney1;
		level1 += exp1/expPerLevel;
		assertTrue(Long.parseLong(downUpEquipStar1.getProp("player.money")) == money1);
		assertTrue(Long.parseLong(downUpEquipStar1.getProp("player.coin")) == coin1);
		assertTrue(Long.parseLong(downUpEquipStar1.getProp("player.exp")) == exp1);
		assertTrue(Long.parseLong(downUpEquipStar1.getProp("player.level")) == level1);
		assertTrue(Long.parseLong(downUpEquipStar1.getProp("equip.star#"+dbEquip01.id)) == equStar01);
		
		assertTrue(exp1 == 32);
		assertTrue(level1 == 3);
		assertTrue(exp2 == 21);
		assertTrue(level2 == 2);
	}
	
	private void init(){
		// 每个线程初始化一次
		NFIObject user = new NFObject(usrOid);
		NFIPropertyManager userPropMgr = user.getPropertyManager();
		userPropMgr.addProperty("uid", 0);
		userPropMgr.addProperty("username", 0);
		NFIProperty propPwd = userPropMgr.addProperty("password", 0);
		propPwd.addCallback(new LimitHandler(this));
		userPropMgr.addProperty("limit", 0);
		put(usrOid, user);
		
		NFIObject player = new NFObject(plyOid);
		NFIPropertyManager playerPropMgr = player.getPropertyManager();
		playerPropMgr.addProperty("uid", 0);
		playerPropMgr.addProperty("nickname", 0);
		NFIProperty propExp = playerPropMgr.addProperty("exp", 0);
		propExp.addCallback(new LevelUpHandler(this));
		playerPropMgr.addProperty("coin", 0);
		playerPropMgr.addProperty("money", 0);
		NFIProperty propCharge = playerPropMgr.addProperty("charge", 0);
		propCharge.addCallback(new VipLevelUpHandler(this));
		playerPropMgr.addProperty("level", 0);
		NFIProperty propVip = playerPropMgr.addProperty("vip", 0);
		propVip.addCallback(new PrivilegeHandler(this));
		NFIProperty propQuest = playerPropMgr.addProperty("quest", 0);
		propQuest.addCallback(new QuestRewardHandler(this));
		playerPropMgr.addProperty("title", 0);
		put(plyOid, player);
		
		NFIObject equip = new NFObject(equOid);
		NFIPropertyManager equipPropMgr = equip.getPropertyManager();
		equipPropMgr.addProperty("id", 0);
		equipPropMgr.addProperty("uid", 0);
		equipPropMgr.addProperty("type", 0);
		equipPropMgr.addProperty("level", 0);
		NFIProperty propEquipExp = equipPropMgr.addProperty("exp", 0);
		propEquipExp.addCallback(new EquipLevelUpHandler(this));
		equipPropMgr.addProperty("star", 0);
		put(equOid, equip);
	}
	
	private long generateUid(){
		return uidBase.incrementAndGet();
	}
	
	private long generateEid(){
		return eidBase.incrementAndGet();
	}
	
	private static final long initCoin = 100;
	private static final byte initLimit = 3;
	private static final List<Byte> initEquipTypes = new ArrayList<Byte>(2);
	static{
		initEquipTypes.add((byte) 0);
		initEquipTypes.add((byte) 1);
	}
	private static final long questCoin = 25;
	private static final long questExp = 7;
	private static final int expPerLevel = 10;
	private static final int expPerEquipLevel = 10;
	private static final int chargePerVipLevel = 50;
	
	private DBUser getUser(String username){
		DBUser dbUser = getCacheUser();
		if (dbUser == null){
			dbUser = tabUser.get(username);
			if (dbUser == null){
				return null;
			}
			// 更新缓存
			setCache(dbUser);
		}else{
			assertTrue(dbUser.username.equals(username));
		}
		
		NFIObject user = get(usrOid);
		assertTrue(user != null);
		
		// 初始化user
		user.getProperty("uid").get().set(dbUser.uid);
		user.getProperty("username").get().set(dbUser.username);
		user.getProperty("password").get().set(dbUser.password);
		user.getProperty("limit").get().set(dbUser.limit);

		return dbUser;
	}
	
	private void updateUser(DBUser dbUser){
		// 更新数据库
		tabUser.put(dbUser.username, dbUser);
		
		// 更新缓存
		setCache(dbUser);
		
		// 处理属性变更
		NFIObject user = get(usrOid);
		assertTrue(user != null);
		
		// uid 和 username不会变更
		user.setProperty("password", dbUser.password);
		user.setProperty("limit", dbUser.limit);
	}
	
	private DBPlayer getPlayer(long uid){
		DBPlayer dbPly = getCachePlayer();
		if (dbPly == null){
			dbPly = tabPlayer.get(uid);
			if (dbPly == null){
				return null;
			}
			// 更新缓存
			setCache(dbPly);
		}else{
			dbPly.uid = uid;
		}
		
		NFIObject player = get(plyOid);
		assertTrue(player != null);
		
		// 初始化player
		player.getProperty("uid").get().set(dbPly.uid);
		player.getProperty("nickname").get().set(dbPly.nickname);
		player.getProperty("exp").get().set(dbPly.exp);
		player.getProperty("coin").get().set(dbPly.coin);
		player.getProperty("money").get().set(dbPly.money);
		player.getProperty("charge").get().set(dbPly.charge);
		player.getProperty("level").get().set(dbPly.level);
		player.getProperty("vip").get().set(dbPly.vip);
		player.getProperty("quest").get().set(dbPly.quest);
		player.getProperty("title").get().set(dbPly.title);
		return dbPly;
	}
	
	private void updatePlayer(DBPlayer dbPly){
		// 更新到数据库
		tabPlayer.put(dbPly.uid, dbPly);
		
		// 更新缓存
		setCache(dbPly);
		
		// 处理属性变更
		NFIObject player = get(plyOid);
		assertTrue(player != null);
		
		// uid不会变更
		player.setProperty("nickname", dbPly.nickname);
		player.setProperty("exp", dbPly.exp);
		player.setProperty("coin", dbPly.coin);
		player.setProperty("money", dbPly.money);
		player.setProperty("charge", dbPly.charge);
		player.setProperty("level", dbPly.level);
		player.setProperty("vip", dbPly.vip);
		player.setProperty("quest", dbPly.quest);
		player.setProperty("title", dbPly.title);
	}
	
	private List<DBEquip> getAllEquips(long uid){
		List<DBEquip> equips = getCacheAllEquips();
		if (equips == null){
			equips = tabEquip.get(uid);
			if (equips == null){
				return null;
			}
			// 更新缓存
			setCache(equips);
		}
		return equips;
	}
	
	private DBEquip getEquip(List<DBEquip> equips, long id){
		if (equips == null || equips.isEmpty()){
			return null;
		}
		
		DBEquip dbEquip = null;
		for (DBEquip equip : equips){
			if (equip.id == id){
				dbEquip = equip;
				break;
			}
		}
		if (dbEquip != null){
			NFIObject equip = get(equOid);
			assertTrue(equip != null);
			
			// 初始化equip
			equip.getProperty("id").get().set(dbEquip.id);
			equip.getProperty("uid").get().set(dbEquip.uid);
			equip.getProperty("type").get().set(dbEquip.type);
			equip.getProperty("level").get().set(dbEquip.level);
			equip.getProperty("exp").get().set(dbEquip.exp);
			equip.getProperty("star").get().set(dbEquip.star);
		}
		return dbEquip;
	}
	
	private void updateEquip(List<DBEquip> equips, DBEquip dbEquip){
		assertTrue(equips != null && !equips.isEmpty());
		assertTrue(dbEquip != null);
		long uid = dbEquip.uid;
		
		// 更新到数据库
		for (int i=0; i<equips.size(); ++i){
			if (equips.get(i).id == dbEquip.id){
				equips.set(i, dbEquip);
				break;
			}
		}
		tabEquip.put(uid, equips);
		
		// 更新缓存
		setCache(equips);
		
		// 处理属性变更
		NFIObject equip = get(equOid);
		assertTrue(equip != null);
		
		// id、uid、type不会变更
		equip.setProperty("level", dbEquip.level);
		equip.setProperty("exp", dbEquip.exp);
		equip.setProperty("star", dbEquip.star);
	}
	
	public void handleLimit(NFGUID oid, String propName, NFIData oldVar, NFIData newVar){		
		assertTrue(propName.equals("password"));
		DBUser dbUser = getCacheUser();
		assertTrue(dbUser != null);
		
		dbUser.limit--;
		NetDown down = getNetDown();
		assertTrue(down != null);
		down.addProp("user.limit", String.valueOf(dbUser.limit));
		updateUser(dbUser);
	}
	
	public void handleLevelUp(NFGUID oid, String propName, NFIData oldVar, NFIData newVar){
		assertTrue(propName.equals("exp"));
		DBPlayer dbPly = getCachePlayer();
		assertTrue(dbPly != null);
		
		// 简单的升级规则：每expPerLevel点经验值涨1级
		int oldLv = (int) (oldVar.getInt() / expPerLevel);
		int newLv = (int) (newVar.getInt() / expPerLevel);
		if (oldLv == newLv){
			return;
		}
		dbPly.level = newLv;
		NetDown down = getNetDown();
		assertTrue(down != null);
		down.addProp("player.level", String.valueOf(dbPly.level));
		updatePlayer(dbPly);
	}
	
	public void handleVipLevelUp(NFGUID oid, String propName, NFIData oldVar, NFIData newVar){
		assertTrue(propName.equals("charge"));
		DBPlayer dbPly = getCachePlayer();
		assertTrue(dbPly != null);
		
		// 简单的vip升级规则：每充chargePerVipLevel点代币涨1级vip
		int oldVip = (int) (oldVar.getInt() / chargePerVipLevel);
		int newVip = (int) (newVar.getInt() / chargePerVipLevel);
		if (oldVip == newVip){
			return;
		}
		dbPly.vip = newVip;
		NetDown down = getNetDown();
		assertTrue(down != null);
		down.addProp("player.vip", String.valueOf(dbPly.vip));
		updatePlayer(dbPly);
	}
	
	public void handleEquipLevelUp(NFGUID oid, String propName, NFIData oldVar, NFIData newVar){
		assertTrue(propName.equals("exp"));
		List<DBEquip> dbEquips = getCacheAllEquips();
		assertTrue(dbEquips != null);
		assertTrue(oid.equals(equOid));
		
		DBEquip dbEquip = getEquip(dbEquips, get(equOid).getProperty("id").getInt());
		assertTrue(dbEquip != null);
		
		// 简单的升级规则：每expPerEquipLevel点金币涨1级
		int oldLv = (int) (oldVar.getInt() / expPerEquipLevel);
		int newLv = (int) (newVar.getInt() / expPerEquipLevel);
		if (oldLv == newLv){
			return;
		}
		
		dbEquip.level = newLv;
		NetDown down = getNetDown();
		assertTrue(down != null);
		down.addProp("equip.level#"+dbEquip.id, String.valueOf(dbEquip.level));
		updateEquip(dbEquips, dbEquip);
	}
	
	public void handlePrivilege(NFGUID oid, String propName, NFIData oldVar, NFIData newVar){
		assertTrue(propName.equals("vip"));
		DBPlayer dbPly = getCachePlayer();
		assertTrue(dbPly != null);
		
		int vip = (int) newVar.getInt();
		switch (vip){
		case 1:{
			dbPly.title = "小康";
			break;
		}case 2:{
			dbPly.title = "中产阶级";
			break;
		}case 3:{
			dbPly.title = "土豪";
			break;
		}case 4:{
			dbPly.title = "大富豪";
			break;
		}default:{
			assertTrue(vip > 4);
			dbPly.title = "大富豪";
			break;
		}}
		NetDown down = getNetDown();
		assertTrue(down != null);
		down.addProp("player.title", dbPly.title);
		updatePlayer(dbPly);
	}
	
	public void handleQuestReward(NFGUID oid, String propName, NFIData oldVar, NFIData newVar){
		assertTrue(propName.equals("quest"));
		DBPlayer dbPly = getCachePlayer();
		assertTrue(dbPly != null);
		
		// 任务完成固定奖励玩家经验和金币
		dbPly.exp += questExp;
		dbPly.coin += questCoin;
		NetDown down = getNetDown();
		assertTrue(down != null);
		down.addProp("player.exp", String.valueOf(dbPly.exp));
		down.addProp("player.coin", String.valueOf(dbPly.coin));
		updatePlayer(dbPly);
	}
	
	@SuppressWarnings("unchecked")
	private <T> T returnError(ErrorCode errc){
		NetDown down = getNetDown();
		assertTrue(down != null);
		down.setErrc(errc);
		return (T) down;
	}
	
	/**
	 * 注册/登录
	 * @param username
	 * @param password
	 * @return uid
	 */
	private NetDownLogin login(String username, String password){
		try{
			NetDownLogin down = new NetDownLogin();
			// 从db查找用户
			DBUser dbUser = getUser(username);
			if (dbUser == null){
				// 注册
				long uid = generateUid();
				
				dbUser = new DBUser();
				dbUser.username = username;
				dbUser.password = password;
				dbUser.limit = initLimit;
				dbUser.uid = uid;
				tabUser.put(username, dbUser);
				
				// player初始数据
				DBPlayer dbPly = new DBPlayer();
				dbPly.uid = uid;
				dbPly.nickname = dbUser.username;
				dbPly.coin = initCoin;
				tabPlayer.put(uid, dbPly);
				
				// 初始装备
				List<DBEquip> equips = new ArrayList<DBEquip>(initEquipTypes.size());
				for (byte type : initEquipTypes){
					DBEquip equip = new DBEquip();
					equip.id = generateEid();
					equip.uid = uid;
					equip.type = type;
					equips.add(equip);
				}
				tabEquip.put(uid, equips);
				
				down.setDbPly(dbPly);
				down.setDbAllEquips(equips);
			}else{
				if (!dbUser.password.equals(password)){
					return returnError(ErrorCode.VERIFY_FAILED);
				}
				
				long uid = dbUser.uid;
				down.setDbPly(getPlayer(uid));
				down.setDbAllEquips(getAllEquips(uid));
			}

			down.setUid(dbUser.uid);
			down.setLimit(dbUser.limit);
			return down;
		}catch (Exception e){
			return returnError(ErrorCode.SERVER_EXCEPTION);
		}finally{
			clearCache();
		}
	}
	
	/**
	 * 修改密码
	 * @param username
	 * @param oldPwd
	 * @param newPwd
	 */
	private NetDownChangePwd changePassword(String username, String oldPwd, String newPwd){
		try{
			NetDownChangePwd down = new NetDownChangePwd();
			DBUser dbUser = getUser(username);
			if (dbUser == null){
				return returnError(ErrorCode.USER_NOT_FOUND);
			}
			
			if (dbUser.limit <= 0){
				return returnError(ErrorCode.OUT_OF_LIMIT);
			}
			
			if (!dbUser.password.equals(oldPwd)){
				return returnError(ErrorCode.VERIFY_FAILED);
			}
			
			if (oldPwd.equals(newPwd)){
				return returnError(ErrorCode.PWD_INVALID);
			}
	
			dbUser.password = newPwd;
			updateUser(dbUser);
			return down;
		}catch (Exception e){
			return returnError(ErrorCode.SERVER_EXCEPTION);
		}finally{
			clearCache();
		}
	}
	
	/**
	 * 完成任务，每次顺序完成一个任务
	 * @param uid
	 */
	private NetDownCompleteQuest completeQuest(long uid){
		try{
			NetDownCompleteQuest down = new NetDownCompleteQuest();
			DBPlayer dbPly = getPlayer(uid);
			if (dbPly == null){
				return returnError(ErrorCode.USER_NOT_FOUND);
			}
			
			dbPly.quest++;
			down.addProp("player.quest", String.valueOf(dbPly.quest));
			updatePlayer(dbPly);
			return down;
		}catch (Exception e){
			return returnError(ErrorCode.SERVER_EXCEPTION);
		}finally{
			clearCache();
		}
	}
	
	/**
	 * 充值
	 * @param uid
	 * @param money
	 */
	private NetDownCharge charge(long uid, int money){
		try{
			NetDownCharge down = new NetDownCharge();
			DBPlayer dbPly = getPlayer(uid);
			if (dbPly == null){
				return returnError(ErrorCode.USER_NOT_FOUND);
			}
			
			dbPly.money += money;
			dbPly.charge += money;
			down.addProp("player.money", String.valueOf(dbPly.money));
			down.addProp("player.charge", String.valueOf(dbPly.charge));
			updatePlayer(dbPly);
			return down;
		}catch (Exception e){
			return returnError(ErrorCode.SERVER_EXCEPTION);
		}finally{
			clearCache();
		}
	}

	/**
	 * 花费金币升级装备
	 * @param uid
	 * @param id
	 * @param consumeCoin
	 */
	private NetDownUpgradeEquip upgradeEquip(long uid, long id, long consumeCoin){
		try{
			NetDownUpgradeEquip down = new NetDownUpgradeEquip();
			DBPlayer dbPly = getPlayer(uid);
			if (dbPly == null){
				return returnError(ErrorCode.USER_NOT_FOUND);
			}
			List<DBEquip> equips = getAllEquips(uid);
			DBEquip dbEquip = getEquip(equips, id);
			if (dbEquip == null){
				return returnError(ErrorCode.EQUIP_NOT_FOUND);
			}
			
			if (dbPly.coin == 0){
				return returnError(ErrorCode.COIN_NOT_ENOUGH);
			}
			
			if (dbPly.coin < consumeCoin){
				consumeCoin = dbPly.coin;
			}
			
			// 消耗玩家金币
			dbPly.coin -= consumeCoin;
			down.addProp("player.coin", String.valueOf(dbPly.coin));
			
			// 增加装备经验
			dbEquip.exp += consumeCoin;
			down.addProp("equip.exp#"+dbEquip.id, String.valueOf(dbEquip.exp));
			
			updatePlayer(dbPly);
			updateEquip(equips, dbEquip);
			return down;
		}catch (Exception e){
			return returnError(ErrorCode.SERVER_EXCEPTION);
		}finally{
			clearCache();
		}
	}

	/**
	 * 花费代币和金币升级装备星级
	 * @param uid
	 * @param id
	 * @param consumeCoin
	 */
	private NetDownUpgradeEquipStar upgradeEquipStar(long uid, long id, long consumeMoney){
		try{
			NetDownUpgradeEquipStar down = new NetDownUpgradeEquipStar();
			DBPlayer dbPly = getPlayer(uid);
			if (dbPly == null){
				return returnError(ErrorCode.USER_NOT_FOUND);
			}
			List<DBEquip> equips = getAllEquips(uid);
			DBEquip dbEquip = getEquip(equips, id);
			if (dbEquip == null){
				return returnError(ErrorCode.EQUIP_NOT_FOUND);
			}
			
			if (dbPly.money == 0){
				return returnError(ErrorCode.MONEY_NOT_ENOUGH);
			}
			
			if (dbPly.money < consumeMoney){
				consumeMoney = dbPly.money;
			}
			
			// 金币必须消耗和代币同样的值
			long consumeCoin = consumeMoney;
			if (dbPly.coin < consumeCoin){
				return returnError(ErrorCode.COIN_NOT_ENOUGH);
			}
			
			// 消耗代币和金币
			dbPly.money -= consumeMoney;
			dbPly.coin -= consumeCoin;
			down.addProp("player.money", String.valueOf(dbPly.money));
			down.addProp("player.coin", String.valueOf(dbPly.coin));
			
			// 增加装备星级和人物经验
			long updStar = consumeMoney;
			long updExp = consumeMoney;
			dbEquip.star += updStar;
			dbPly.exp += updExp;
			down.addProp("equip.star#"+dbEquip.id, String.valueOf(dbEquip.star));
			down.addProp("player.exp", String.valueOf(dbPly.exp));
			
			updatePlayer(dbPly);
			updateEquip(equips, dbEquip);
			return down;
		}catch (Exception e){
			return returnError(ErrorCode.SERVER_EXCEPTION);
		}finally{
			clearCache();
		}
	}
}
