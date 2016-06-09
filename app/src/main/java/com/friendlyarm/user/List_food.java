package com.friendlyarm.user;

/**
 * 采购清单
 * Created by Q on 2016-05-17.
 *
 * //必备食品清单
 *      http://wechi.wehefei.com/201310/1113615231.html
 * //冰箱内常备七种食品
 *      http://shipin.people.com.cn/n/2014/1027/c85914-25911949.html
 */
public class List_food {
    /**
     * 鱼肉营养丰富，脂肪含量低，可就是做起来太麻烦。可以在超市买几包真空冷冻的速冻鱼肉片，放在冰箱冷冻室里。
     * 吃之前提前8—10小时放入冷藏室解冻。如果赶时间，可以直接用微波炉解冻。这种鱼片是做水煮鱼、炒鱼片的好材料。
     * 除了速冻鱼肉，速冻虾仁也可以买一些，做菜百搭且方便。
     */
    private String name_Forzen_fillets = "速冻鱼肉片";
    private String name_vegetable = "蔬菜";
    private String name_bread = "全麦馒头或全麦面包";
    private String name_meat = "肉馅或鸡翅";
    private String name_nut = "坚果";
    private String name_dairy_food = "乳品";
    private String name_egg = "鸡蛋";
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName_Forzen_fillets() {
        return name_Forzen_fillets;
    }

    public String getName_vegetable() {
        return name_vegetable;
    }

    public String getName_bread() {
        return name_bread;
    }

    public String getName_meat() {
        return name_meat;
    }

    public String getName_nut() {
        return name_nut;
    }

    public String getName_dairy_food() {
        return name_dairy_food;
    }

    public String getName_egg() {
        return name_egg;
    }

    @Override
    public String toString() {
        return "List_food{" +
                "name_Forzen_fillets='" + name_Forzen_fillets + '\'' +
                ", name_vegetable='" + name_vegetable + '\'' +
                ", name_bread='" + name_bread + '\'' +
                ", name_meat='" + name_meat + '\'' +
                ", name_nut='" + name_nut + '\'' +
                ", name_dairy_food='" + name_dairy_food + '\'' +
                ", name_egg='" + name_egg + '\'' +
                ", id=" + id +
                '}';
    }
}
