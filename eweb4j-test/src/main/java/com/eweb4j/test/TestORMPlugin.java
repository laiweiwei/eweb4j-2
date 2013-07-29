package com.eweb4j.test;

import java.util.List;

import com.eweb4j.core.EWeb4J;
import com.eweb4j.core.configurator.Storage;
import com.eweb4j.core.configurator.MapStorage;
import com.eweb4j.core.plugin.Plugin;
import com.eweb4j.core.plugin.PluginManager;
import com.eweb4j.core.plugin.PluginManagerImpl;
import com.eweb4j.jdbc.config.DSBean;
import com.eweb4j.jdbc.config.DSPool;
import com.eweb4j.orm.helper.Db;
import com.eweb4j.orm.plugin.DruidPlugin;
import com.eweb4j.orm.plugin.ORMPlugin;

/**
 * 测试ORM插件功能
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 上午11:15:27
 */
public class TestORMPlugin {

	public static void main(String[] args) throws Exception{
		
		//准备好监听器
		EWeb4J.Listener listener = new EWeb4J.Listener() {
			public void onStartup(PluginManager pluginManager) {
				//安装 ORM 插件
				pluginManager.install(new ORMPlugin());
				//安装 druid 插件
				pluginManager.install(new DruidPlugin());
			}
			
			public void onShutdown(PluginManager plugins){
				//停止所有插件
				plugins.stopAll();
			}
		};
		
		//构建插件仓库
		final Storage<String, Plugin> pluginStorage = new MapStorage<String, Plugin>();
		//构建配置仓库
		final Storage<String, Object> configStorage = new MapStorage<String, Object>();
		//构建插件管理器
		final PluginManager pluginManager = new PluginManagerImpl(pluginStorage, configStorage);
		//构建框架实例
		EWeb4J eweb4j = new EWeb4J(pluginManager);
		//启动框架
		eweb4j.startup(listener);
		
		DSBean dsBean = DSPool.first();
		System.out.println("first ds->"+dsBean.name);
		
//		final String eql = "select * from #table where #nickname = ?";
//		List<Pets> pets = Db.ar(Pets.class).query(eql, "testName");
//		System.out.println("pet->"+pets);
		
		List<Pets> pets = 
			Db.ar(Pets.class)
			    .alias("p")
			    .join("user", "u")
			    .query("select p.* from #p.table p, #u.table u where #p.user = #u.id and #u.pwd = ? order by #p.id asc", 123);
		System.out.println("pet->"+pets);
//		//获取数据源 ds_1
//		DataSource ds1 = DSPool.get("ds_1").ds;
//		int r = JDBCHelper.execute(ds1, "update t_pet set create_at = ? where id = ?", new Date(), 29);
//		System.out.println("update rows->"+r);
//		
//		List<JDBCRow> rows = JDBCHelper.find(ds1, "select * from t_pet");
//		for (JDBCRow row : rows) {
//			int num = row.number();
//			System.out.println("r->" + num);
//			for (JDBCColumn col : row.columns()){
//				System.out.println("\t"+col);
//			}
//		}
//		
//		int[] rr = JDBCHelper.batchExecute(ds1, "insert into t_pet(name, age) values(?,?)", new Object[][]{new Object[]{1,2}, new Object[]{1,2}});
//		for (int n : rr)
//			System.out.println("after insert, id->"+n);
//		
//		r = JDBCHelper.execute(ds1, "delete from t_pet where name = ? and age = ?", 1, 2);
//		System.out.println("delete rows->"+r);
//		
//		//获取数据源 ds_2
//		DataSource ds2 = DSPool.get("ds_2").ds;
//		JDBCRow user = JDBCHelper.findOne(ds2, "select * from t_user");
//		System.out.println("user->" + user.number());
//		for (JDBCColumn col : user.columns()){
//			System.out.println("\t"+col);
//		}
		
		//停止框架
		eweb4j.shutdown(listener);
	}
	
}
