package uga.cs4370.sleighlist.models;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

/**
 * View models used by the Mustache templates. Mustache calls the record
 * accessor / helper methods by name, e.g. {{priceLabel}}.
 *
 * Backend note: the DAO layer should build these same objects from
 * ResultSets so the templates don't need to change.
 */
public final class Models {

    private Models() {}

    private static final NumberFormat USD = NumberFormat.getCurrencyInstance(Locale.US);
    private static final NumberFormat COUNT = NumberFormat.getIntegerInstance(Locale.US);

    public static String money(double v) {
        return USD.format(v);
    }

    public static String count(long v) {
        return COUNT.format(v);
    }

    public record Member(int id, String name, String color) {
        public String initials() {
            String[] parts = name.split(" ");
            return parts.length > 1
                    ? "" + parts[0].charAt(0) + parts[parts.length - 1].charAt(0)
                    : name.substring(0, 1);
        }
        public String firstName() {
            return name.split(" ")[0];
        }
    }

    public record Product(int id, String title, String category, double price, double listPrice,
                          double stars, int reviews, boolean bestseller) {
        public String priceLabel() { return money(price); }
        public boolean onSale() { return listPrice > price; }
        public String listPriceLabel() { return money(listPrice); }
        public String starsLabel() { return String.format("%.1f", stars); }
        public int starsPct() { return (int) Math.round(stars / 5.0 * 100); }
        public String reviewsLabel() { return count(reviews); }
        public String initial() { return title.substring(0, 1); }
        /** Hue per category so placeholder thumbnails are colour-coded (until real images load). */
        public int hue() {
            return switch (category) {
                case "Kitchen & Dining" -> 18;
                case "Electronics" -> 205;
                case "Home & Kitchen" -> 35;
                case "Toys & Games" -> 350;
                case "Sports & Outdoors" -> 160;
                case "Patio, Lawn & Garden" -> 100;
                case "Clothing" -> 260;
                case "Books" -> 45;
                case "Beauty & Personal Care" -> 320;
                case "Office Products" -> 185;
                default -> Math.floorMod(category.hashCode(), 360);
            };
        }
    }

    public record Option(String value, String label, boolean selected) {}

    public record Category(String name, long productCount, boolean selected) {
        public String countLabel() { return count(productCount); }
    }

    public record Group(int id, String name, String occasion, LocalDate exchangeDate,
                        List<Member> members, boolean youAreAdmin, String inviteCode,
                        int santaLimit, boolean santaDrawn) {
        public int memberCount() { return members.size(); }
        public long daysLeft() { return ChronoUnit.DAYS.between(LocalDate.now(), exchangeDate); }
        public String exchangeLabel() {
            return exchangeDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.US));
        }
        public List<Member> avatarMembers() { return members.subList(0, Math.min(5, members.size())); }
        public int extraMembers() { return Math.max(0, members.size() - 5); }
        public String santaLimitLabel() { return money(santaLimit); }
    }

    public record WishItem(int id, Product product, boolean mustHave, String note) {}

    public record WishList(int id, String name, String groupName, List<WishItem> items, boolean selected) {
        public int itemCount() { return items.size(); }
        public String totalLabel() {
            return money(items.stream().mapToDouble(i -> i.product().price()).sum());
        }
    }

    /**
     * One item on a friend's list as seen by the viewer. When the viewer is the
     * list owner the controller passes ownerView=true and every claim field is
     * ignored by the template (spoiler rule).
     */
    public record ClaimTag(int itemId, Product product, boolean mustHave, String note,
                           Member claimer, boolean claimedByYou, boolean purchased) {
        public boolean unclaimed() { return claimer == null && !claimedByYou; }
        public boolean claimedByOther() { return claimer != null && !claimedByYou; }
    }

    public record MemberStat(Member member, int items, int unclaimed, double listValue, boolean you) {
        public String valueLabel() { return money(listValue); }
        public boolean allClaimed() { return !you && unclaimed == 0; }
    }

    public record CategoryStat(String name, int count, int pct) {}

    public record Fact(String value, String label) {}

    public record ClaimedGift(int claimId, Product product, boolean purchased) {}

    public record RecipientBudget(Member recipient, String groupName, List<ClaimedGift> gifts, double pctOfBudget) {
        public double total() { return gifts.stream().mapToDouble(g -> g.product().price()).sum(); }
        public String totalLabel() { return money(total()); }
        public String pctLabel() { return String.format("%.2f", pctOfBudget); }
        public int giftCount() { return gifts.size(); }
    }

    public record Exclusion(int id, Member a, Member b, String reason) {}

    public record Eligibility(Member member, int count, String names) {}
}
