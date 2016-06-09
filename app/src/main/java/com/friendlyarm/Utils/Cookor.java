package com.friendlyarm.Utils;
import android.graphics.Bitmap;

public class Cookor {
	private String id;//编号
	private String title;//标题
	private String tags;//
	private String imtro;//
	private String ingredients;//
	private String burden;//
	private Bitmap albums;//
	private Step[] steps;//
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getImtro() {
		return imtro;
	}
	public void setImtro(String imtro) {
		this.imtro = imtro;
	}
	public String getIngredients() {
		return ingredients;
	}
	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}
	public String getBurden() {
		return burden;
	}
	public void setBurden(String burden) {
		this.burden = burden;
	}
	public Bitmap getAlbums() {
		return albums;
	}
	public void setAlbums(Bitmap albums) {
		this.albums = albums;
	}
	public Step[] getSteps() {
		return steps;
	}
	public void setSteps(Step[] steps) {
		this.steps = steps;
	}




	/*
	 * {"data":[{"ingredients":"白菜心,200g","tags":"汤;辣;10分钟内;汤锅;1-2人","id":"6511"
	 * ,"title":"不辣的开水白菜—学做川菜","burden":"白胡椒粉,4g;盐,3g;清鸡汤,200ml","albums":
	 * ["http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/t\/7\/6511_355812.jpg"
	 * ],"imtro":
	 * "春 川菜被大多数人认为是麻辣的，不麻不辣那不是川菜。而这道开水白菜原系川菜名厨黄敬临在清宫御膳房时创制，后黄敬临将此菜制法带回四川，广为流传。成为饭店高档筵席上的一味佳肴，其关键在于吊汤，成菜。 这开水白菜看似简单，其实吊汤的品质很重要，决定着菜的口味。我这是山寨版的，用的自家老鸡汤，没有那么讲究地吊汤，就是用一只老母鸡、一些鲜笋、金华火腿片熬制而成。 鸡汤的营养自不必多说了。"
	 * ,"steps":[{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/66\/6511_f3731d35d1c1f116.jpg"
	 * ,"step":"1.黄芽白菜心1棵"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/66\/6511_044ec4fca1f1ca6c.jpg"
	 * ,"step":"2.用手掰开白菜心，洗净放入锅中。"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/66\/6511_98468e135e8d20a0.jpg"
	 * ,"step":"3.吊制老鸡汤"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/66\/6511_badfb6e16bd43cee.jpg"
	 * ,"step":"4.白菜中放入白胡椒粉，加入鸡汤，煮开，至白菜煮塌了，加盐调味关火。"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/66\/6511_27e12d1399bf80ef.jpg"
	 * ,"step":"5.装盘。"}]},{"ingredients":"瘦肉,80g;木耳,200g;豌豆尖,200g","tags":
	 * "咸香;川菜;辣;鱼香;炒","id":"7855","title":"川菜鱼香肉丝","burden":
	 * "葱,适量;姜,适量;蒜,适量;酱油,适量;盐,适量;淀粉,适量;醋,适量;糖,适量;辣椒,适量","albums":[
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/t\/8\/7855_655278.jpg"
	 * ],"imtro":
	 * "在一本古老的大众川菜菜谱中看到了这道经典的鱼香肉丝，拿出来跟大家分享一下。北方大多在鱼香肉丝中加入胡萝卜等配菜，不够正宗，川菜中用的是木耳。这道菜具有甜酸咸辣和葱姜蒜综合的鱼香味，是比较有代表性的川菜之一。"
	 * ,"steps":[{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/79\/7855_82867b50231a2519.jpg"
	 * ,"step":"1.将猪肉切成肉丝，加入水豆粉和盐，腌一会儿备用"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/79\/7855_590b4bbd3e0c309d.jpg"
	 * ,"step":"2.将盐、酱油、醋、白糖、水豆粉放入碗中调成滋汁备用"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/79\/7855_a1e4f02d949b3f75.jpg"
	 * ,"step":"3.木耳洗净切丝，葱切段"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/79\/7855_b715b7bd8c4d3ff7.jpg"
	 * ,"step":"4.姜、蒜切成颗粒"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/79\/7855_e9400582717ed0d0.jpg"
	 * ,"step":"5.炒锅置于旺火上，加入食用油，七成熟时放入肉丝炒至散籽、发白"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/79\/7855_6caef5d65ace5706.jpg"
	 * ,"step":"6.加入红辣椒、蒜和姜米炒出香味"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/79\/7855_493cb985633de2c2.jpg"
	 * ,"step":"7.下如木耳和葱花翻炒"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/79\/7855_8f9859ff9f34c48a.jpg"
	 * ,"step":"8.放入芥蓝苗（由于没有买到豌豆尖，所以用芥蓝苗代替了）翻炒"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/79\/7855_3afec5b7d2107174.jpg"
	 * ,"step":"9.将滋汁勾匀倒下炒转"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/79\/7855_a58399f760f3ad78.jpg"
	 * ,"step":"10.起锅装盘。"}]},{"ingredients":"牛百叶,300g","tags":
	 * "家常菜;辣;10分钟内;宴请;朋友聚餐;1-2人;锅子;其他工艺","id":"23491","title":"川菜经典之红油牛百叶",
	 * "burden":"?
	 **/

	/**
	 * data:[{ingredients=白菜心,200g, tags=汤;辣;10分钟内;汤锅;1-2人, id=6511,
	 * title=不辣的开水白菜—学做川菜, burden=白胡椒粉,4g;盐,3g;清鸡汤,200ml, albums=[
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/t\/7\/6511_355812.jpg"
	 * ], imtro=春
	 * 川菜被大多数人认为是麻辣的，不麻不辣那不是川菜。而这道开水白菜原系川菜名厨黄敬临在清宫御膳房时创制，后黄敬临将此菜制法带回四川，广为流传。
	 * 成为饭店高档筵席上的一味佳肴，其关键在于吊汤，成菜。
	 * 这开水白菜看似简单，其实吊汤的品质很重要，决定着菜的口味。我这是山寨版的，用的自家老鸡汤，没有那么讲究地吊汤，就是用一只老母鸡、一些鲜笋、
	 * 金华火腿片熬制而成。 鸡汤的营养自不必多说了。, steps=[{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/66\/6511_f3731d35d1c1f116.jpg"
	 * ,"step":"1.黄芽白菜心1棵"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/66\/6511_044ec4fca1f1ca6c.jpg"
	 * ,"step":"2.用手掰开白菜心，洗净放入锅中。"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/66\/6511_98468e135e8d20a0.jpg"
	 * ,"step":"3.吊制老鸡汤"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/66\/6511_badfb6e16bd43cee.jpg"
	 * ,"step":"4.白菜中放入白胡椒粉，加入鸡汤，煮开，至白菜煮塌了，加盐调味关火。"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/66\/6511_27e12d1399bf80ef.jpg"
	 * ,"step":"5.装盘。"}]}, {ingredients=瘦肉,80g;木耳,200g;豌豆尖,200g,
	 * tags=咸香;川菜;辣;鱼香;炒, id=7855, title=川菜鱼香肉丝,
	 * burden=葱,适量;姜,适量;蒜,适量;酱油,适量;盐,适量;淀粉,适量;醋,适量;糖,适量;辣椒,适量, albums=[
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/t\/8\/7855_655278.jpg"
	 * ], imtro=在一本古老的大众川菜菜谱中看到了这道经典的鱼香肉丝，拿出来跟大家分享一下。北方大多在鱼香肉丝中加入胡萝卜等配菜，不够正宗，
	 * 川菜中用的是木耳。这道菜具有甜酸咸辣和葱姜蒜综合的鱼香味，是比较有代表性的川菜之一。, steps=[{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/79\/7855_82867b50231a2519.jpg"
	 * ,"step":"1.将猪肉切成肉丝，加入水豆粉和盐，腌一会儿备用"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/79\/7855_590b4bbd3e0c309d.jpg"
	 * ,"step":"2.将盐、酱油、醋、白糖、水豆粉放入碗中调成滋汁备用"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/79\/7855_a1e4f02d949b3f75.jpg"
	 * ,"step":"3.木耳洗净切丝，葱切段"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/79\/7855_b715b7bd8c4d3ff7.jpg"
	 * ,"step":"4.姜、蒜切成颗粒"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/79\/7855_e9400582717ed0d0.jpg"
	 * ,"step":"5.炒锅置于旺火上，加入食用油，七成熟时放入肉丝炒至散籽、发白"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/79\/7855_6caef5d65ace5706.jpg"
	 * ,"step":"6.加入红辣椒、蒜和姜米炒出香味"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/79\/7855_493cb985633de2c2.jpg"
	 * ,"step":"7.下如木耳和葱花翻炒"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/79\/7855_8f9859ff9f34c48a.jpg"
	 * ,"step":"8.放入芥蓝苗（由于没有买到豌豆尖，所以用芥蓝苗代替了）翻炒"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/79\/7855_3afec5b7d2107174.jpg"
	 * ,"step":"9.将滋汁勾匀倒下炒转"},{"img":
	 * "http:\/\/juheimg.oss-cn-hangzhou.aliyuncs.com\/cookbook\/s\/79\/7855_a58399f760f3ad78.jpg"
	 * ,"step":"10.起锅装盘。"}]}, {ingredients=牛百叶,300g,
	 * tags=家常菜;辣;10分钟内;宴请;朋友聚餐;1-2人;锅子;其他工艺, id=23491, title=川菜经典之红油牛百叶,
	 * burden=干辣椒,10g;姜,4片;蒜,适量
	 */
}
