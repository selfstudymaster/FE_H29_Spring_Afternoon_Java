package main;
// H29春季 基本情報 午後 Java
// [プログラム 1]
abstract class TierTable { // /* (a) */ class TierTable
    final double[][] pairs;

    TierTable(double... tiers) throws IllegalAccessException {
        if (tiers.length % 2 == 1) {
            throw new IllegalAccessException("不正な長さ" + tiers.length);
        }
        double[][] a = new double[tiers.length / 2][];
        for (int i =0; i < tiers.length; i += 2) {
            a[i / 2] = new double[] {tiers[i], tiers[i + 1]}; // a[/* (b) */] = new double[] {tiers[i], tiers[i + 1]};
        }
        this.pairs = a;
    }

    abstract double map(double amount) throws IllegalAccessException;
}


// [プログラム 2]
class TieredRateTable extends TierTable {
    TieredRateTable(double... tiers) throws IllegalAccessException {
        super(tiers);
    }

    double map(double amount) {
        double charge = 0;
        for (int i = 0; i < pairs.length; i++) {
            if (i + 1 < pairs.length && amount > pairs[i + 1][0]) {
                charge += (pairs[i + 1][0] - pairs[i][0]) * pairs[i][1]; // charge += (pairs/* (c) */ - pairs/* (d) */) * pairs[i][1];
            } else {
                charge += (amount - pairs[i][0]) * pairs[i][1];
                break;
            }
        }
        return charge;
    }
}

// [プログラム 3]
class RatePlan {
    private final String name;
    private final double basicCharge;
    private final TierTable pricingTiers;

    RatePlan(String name, double basicCharge, TierTable pricingTiers) {
        this.name = name;
        this.basicCharge = basicCharge;
        this.pricingTiers = pricingTiers;
    }

    String getName() {
        return name;
    }

    int getPrice(double amount) throws IllegalAccessException {
        return (int) (basicCharge + pricingTiers.map(amount));
    }
}


// [プログラム 4]
public class Main {
    private static void main(String[] args) throws IllegalAccessException {
        RatePlan planA = new RatePlan("プランA", 1123.30, new TieredRateTable(0, 19.62, 120, 26.10, 300, 30.12));
        RatePlan planB = new RatePlan("プランB", 1040.10, new TieredRateTable(0, 18.17, 120, 24.17, 300, 27.77));

        double amount = 543.0;
        int d = planA.getPrice(amount) - planB.getPrice(amount);
        if (d < 0) {
            System.out.printf("%sが%d円安い%n", planA.getName(), -d);
        } else if (d > 0) {
            System.out.printf("%sが%d円安い%n", planB.getName(), d);
        } else {
            System.out.println("両プランで同額");
        }
    }
}


// [プログラム 5]
class DiscountTable extends TierTable {
    DiscountTable(double... tiers) throws IllegalAccessException {
        super(tiers);
    }

    double map(double amount) throws IllegalAccessException {
        for (int i = pairs.length - 1; i >= 0; i--) {
            if (amount >= pairs[i][0]) {
                return pairs[i][1];
            }
        }
        throw new IllegalAccessException("amount = " + amount);
    }
}


// [プログラム 6]

class DiscountPlan extends RatePlan { // class DiscountPlan /* (e) */ RatePlan
    private final TierTable discountTiers;

    DiscountPlan(String name, double basicCharge, TierTable pricingTiers, TierTable discountTiers) {
        super(name, basicCharge, pricingTiers);
        this.discountTiers = discountTiers;
    }

    int getPrice(double amount) throws IllegalAccessException {
        int price = super.getPrice(amount); // int price = /* (f) */.getPrice(amount);
        return (int) (price * (1.0 - discountTiers.map(price))); // return (int) (price * /* (g) */);
    }
}
