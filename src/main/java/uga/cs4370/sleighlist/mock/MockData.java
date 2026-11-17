package uga.cs4370.sleighlist.mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import uga.cs4370.sleighlist.models.Models.*;

/**
 * Hard-coded sample data so every page renders before the database exists.
 * Each method here stands in for a future DAO / service call. Delete this
 * class once the controllers are wired to JDBC.
 */
public final class MockData {

    private MockData() {}

    // ---------------------------------------------------------------- users
    public static final Member JORDAN = new Member(1, "Jordan Park", "#2F6B5A");
    public static final Member MAYA = new Member(2, "Maya Park", "#A8233A");
    public static final Member SAM = new Member(3, "Sam Park", "#3D5A99");
    public static final Member ROSE = new Member(4, "Rose Park", "#8A5A2B");
    public static final Member LEO = new Member(5, "Leo Chen", "#6B4C9A");
    public static final Member PRIYA = new Member(6, "Priya Shah", "#B5651D");
    public static final Member THEO = new Member(7, "Theo Alvarez", "#2C7A8C");
    public static final Member NINA = new Member(8, "Nina Park", "#9A3D6B");
    public static final Member ARI = new Member(9, "Ari Novak", "#5C7A2C");
    public static final Member BEA = new Member(10, "Bea Lindqvist", "#7A2C4F");
    public static final Member MARCUS = new Member(11, "Marcus Hill", "#4F5D75");

    /** The logged-in user for the prototype. */
    public static Member currentUser() {
        return JORDAN;
    }

    public static Member member(int id) {
        return List.of(JORDAN, MAYA, SAM, ROSE, LEO, PRIYA, THEO, NINA, ARI, BEA, MARCUS).stream()
                .filter(m -> m.id() == id).findFirst().orElse(MAYA);
    }

    // ------------------------------------------------------------- products
    private static final List<Product> PRODUCTS = List.of(
            new Product(101, "Stanley Quencher H2.0 FlowState Tumbler, 40 oz", "Kitchen & Dining", 45.00, 45.00, 4.8, 182_340, true),
            new Product(102, "Kindle Paperwhite 16GB, 7\" Glare-Free Display", "Electronics", 149.99, 159.99, 4.7, 96_512, true),
            new Product(103, "Ninja CREAMi Ice Cream Maker, 7 One-Touch Programs", "Kitchen & Dining", 199.99, 229.99, 4.6, 41_208, false),
            new Product(104, "Sherpa Fleece Throw Blanket, 50 x 60 in", "Home & Kitchen", 32.99, 39.99, 4.7, 58_903, false),
            new Product(105, "LEGO Icons Orchid Building Set for Adults", "Toys & Games", 49.99, 49.99, 4.8, 33_115, true),
            new Product(106, "Lodge 12 Inch Cast Iron Skillet, Pre-Seasoned", "Kitchen & Dining", 34.90, 44.90, 4.7, 154_887, false),
            new Product(107, "Ember Mug 2 Temperature Control Smart Mug, 10 oz", "Kitchen & Dining", 129.95, 129.95, 4.4, 27_640, false),
            new Product(108, "Hydro Flask 32 oz Wide Mouth Bottle with Flex Cap", "Sports & Outdoors", 44.95, 44.95, 4.8, 61_372, false),
            new Product(109, "Wool Felt Desk Organizer with Pen Tray", "Office Products", 39.00, 39.00, 4.5, 2_118, false),
            new Product(110, "Plano 3700 Tackle Box with 4 Utility Trays", "Sports & Outdoors", 29.99, 34.99, 4.7, 18_446, false),
            new Product(111, "Weber Precision Grill Tool Set, 3 Pieces", "Patio, Lawn & Garden", 36.99, 36.99, 4.6, 9_870, false),
            new Product(112, "Sony WH-1000XM5 Wireless Noise Canceling Headphones", "Electronics", 329.99, 399.99, 4.5, 22_301, true),
            new Product(113, "Burt's Bees Essential Everyday Beauty Gift Set", "Beauty & Personal Care", 14.97, 14.97, 4.7, 47_015, false),
            new Product(114, "Codenames Party Board Game", "Toys & Games", 19.99, 24.99, 4.8, 88_120, true),
            new Product(115, "Smartwool Hike Light Cushion Crew Socks, 3 Pack", "Clothing", 54.00, 54.00, 4.6, 6_402, false),
            new Product(116, "Instax Mini 12 Instant Camera", "Electronics", 79.95, 79.95, 4.7, 30_884, false),
            new Product(117, "Stoneware Mug Set of 4, 14 oz", "Kitchen & Dining", 88.00, 96.00, 4.8, 4_311, false),
            new Product(118, "Salt, Fat, Acid, Heat: Mastering the Elements of Good Cooking", "Books", 22.49, 40.00, 4.8, 31_590, false),
            new Product(119, "Cuisinart Electric Wine Opener with Foil Cutter", "Kitchen & Dining", 24.95, 29.95, 4.4, 12_907, false),
            new Product(120, "Bluetooth Beanie with Built-in Headphones", "Clothing", 19.99, 25.99, 4.3, 21_066, false),
            new Product(121, "Fishing Lure Advent Calendar, 24 Days", "Sports & Outdoors", 39.99, 39.99, 4.2, 1_874, false),
            new Product(122, "Meat Thermometer Digital Instant Read", "Kitchen & Dining", 17.99, 19.99, 4.7, 103_455, true),
            new Product(123, "Aromatherapy Essential Oil Diffuser, 300 ml", "Home & Kitchen", 25.99, 32.99, 4.5, 74_210, false),
            new Product(124, "Catan Board Game, 25th Anniversary", "Toys & Games", 43.99, 54.99, 4.8, 52_670, false)
    );

    public static Product product(int id) {
        return PRODUCTS.stream().filter(p -> p.id() == id).findFirst().orElseThrow();
    }

    /** Stand-in for the product browser search (category + price range + rating). */
    public static List<Product> searchProducts(String q, String category, Double min, Double max,
                                               Double minStars, boolean bestsellerOnly, String sort) {
        String needle = q == null ? "" : q.trim().toLowerCase();
        Comparator<Product> order = switch (sort == null ? "" : sort) {
            case "price-asc" -> Comparator.comparingDouble(Product::price);
            case "price-desc" -> Comparator.comparingDouble(Product::price).reversed();
            case "rating" -> Comparator.comparingDouble(Product::stars).reversed();
            default -> Comparator.comparingInt(Product::reviews).reversed();
        };
        return PRODUCTS.stream()
                .filter(p -> needle.isEmpty() || p.title().toLowerCase().contains(needle))
                .filter(p -> category == null || category.isBlank() || p.category().equals(category))
                .filter(p -> min == null || p.price() >= min)
                .filter(p -> max == null || p.price() <= max)
                .filter(p -> minStars == null || p.stars() >= minStars)
                .filter(p -> !bestsellerOnly || p.bestseller())
                .sorted(order)
                .collect(Collectors.toList());
    }

    /** Counts are illustrative of the Kaggle subset we plan to load. */
    public static List<Category> categories(String selected) {
        Map<String, Long> counts = new LinkedHashMap<>();
        counts.put("Books", 6_204L);
        counts.put("Beauty & Personal Care", 4_918L);
        counts.put("Clothing", 7_355L);
        counts.put("Electronics", 8_120L);
        counts.put("Home & Kitchen", 5_731L);
        counts.put("Kitchen & Dining", 6_887L);
        counts.put("Office Products", 1_942L);
        counts.put("Patio, Lawn & Garden", 2_265L);
        counts.put("Sports & Outdoors", 3_164L);
        counts.put("Toys & Games", 1_627L);
        List<Category> out = new ArrayList<>();
        counts.forEach((k, v) -> out.add(new Category(k, v, k.equals(selected))));
        return out;
    }

    public static long totalProducts() {
        return categories(null).stream().mapToLong(Category::productCount).sum();
    }

    // --------------------------------------------------------------- groups
    public static List<Group> groups() {
        return List.of(
                new Group(1, "Park Family Christmas", "Christmas", LocalDate.of(2026, 12, 24),
                        List.of(JORDAN, MAYA, SAM, ROSE, NINA, LEO), true, "PARK-7Q2X", 40, false),
                new Group(2, "Apartment 4B", "Holiday swap", LocalDate.of(2026, 12, 18),
                        List.of(PRIYA, JORDAN, THEO), false, "APT4B-K9MD", 25, true),
                new Group(3, "Studio 9 Gift Swap", "White elephant", LocalDate.of(2026, 12, 12),
                        List.of(ARI, BEA, MARCUS, JORDAN, PRIYA), false, "S9-SWAP-31", 30, true)
        );
    }

    public static Group group(int id) {
        return groups().stream().filter(g -> g.id() == id).findFirst().orElse(groups().get(0));
    }

    // ----------------------------------------------------------- wish lists
    public static List<WishList> myLists(int selectedId) {
        List<WishList> lists = List.of(
                new WishList(1, "Christmas 2026", "Park Family Christmas", List.of(
                        new WishItem(1, product(112), true, "Black or silver"),
                        new WishItem(2, product(117), false, ""),
                        new WishItem(3, product(124), true, ""),
                        new WishItem(4, product(118), false, "Hardcover please"),
                        new WishItem(5, product(115), false, "Men's L"),
                        new WishItem(6, product(122), false, ""),
                        new WishItem(7, product(107), true, "10 oz, any color")
                ), false),
                new WishList(2, "Apartment swap", "Apartment 4B", List.of(
                        new WishItem(8, product(123), false, ""),
                        new WishItem(9, product(120), true, "Dark grey"),
                        new WishItem(10, product(119), false, "")
                ), false),
                new WishList(3, "March birthday", "Not shared yet", List.of(
                        new WishItem(11, product(116), true, "Mint green"),
                        new WishItem(12, product(109), false, "")
                ), false)
        );
        return lists.stream()
                .map(l -> new WishList(l.id(), l.name(), l.groupName(), l.items(), l.id() == selectedId))
                .collect(Collectors.toList());
    }

    public static List<Option> myListOptions() {
        return myLists(0).stream()
                .map(l -> new Option(String.valueOf(l.id()), l.name(), l.id() == 1))
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------- friend's list
    /** Maya's list as Jordan sees it (claims visible because Jordan isn't the owner). */
    public static List<ClaimTag> friendList() {
        return List.of(
                new ClaimTag(21, product(102), true, "Signature edition if it's on sale", null, false, false),
                new ClaimTag(22, product(103), true, "", LEO, false, true),
                new ClaimTag(23, product(104), false, "Something in sage or oatmeal", null, true, false),
                new ClaimTag(24, product(116), false, "", SAM, false, false),
                new ClaimTag(25, product(115), false, "Women's M, any color but pink", null, false, false),
                new ClaimTag(26, product(105), false, "", null, true, true),
                new ClaimTag(27, product(118), false, "", null, false, false)
        );
    }

    // ------------------------------------------------------ group dashboard
    public static List<MemberStat> memberStats() {
        return List.of(
                new MemberStat(JORDAN, 7, 0, 686.41, true),
                new MemberStat(MAYA, 7, 3, 589.40, false),
                new MemberStat(SAM, 6, 4, 412.50, false),
                new MemberStat(ROSE, 3, 0, 121.47, false),
                new MemberStat(NINA, 8, 6, 389.20, false),
                new MemberStat(LEO, 5, 2, 560.00, false)
        );
    }

    public static List<CategoryStat> topCategories() {
        int[] counts = {11, 8, 6, 5, 3, 3};
        String[] names = {"Kitchen & Dining", "Electronics", "Home & Kitchen", "Toys & Games", "Clothing", "Books"};
        List<CategoryStat> out = new ArrayList<>();
        for (int i = 0; i < counts.length; i++) {
            out.add(new CategoryStat(names[i], counts[i], counts[i] * 100 / counts[0]));
        }
        return out;
    }

    // --------------------------------------------------------------- budget
    public static double budgetAmount() {
        return 400.00;
    }

    public static List<RecipientBudget> budgetByRecipient() {
        List<RecipientBudget> rows = List.of(
                new RecipientBudget(MAYA, "Park Family Christmas", List.of(
                        new ClaimedGift(501, product(104), false),
                        new ClaimedGift(502, product(105), true)), 0),
                new RecipientBudget(ROSE, "Park Family Christmas", List.of(
                        new ClaimedGift(503, product(117), true)), 0),
                new RecipientBudget(SAM, "Park Family Christmas", List.of(
                        new ClaimedGift(504, product(114), true),
                        new ClaimedGift(505, product(108), false)), 0),
                new RecipientBudget(THEO, "Apartment 4B", List.of(
                        new ClaimedGift(506, product(110), true)), 0),
                new RecipientBudget(PRIYA, "Apartment 4B", List.of(
                        new ClaimedGift(507, product(113), false)), 0)
        );
        double budget = budgetAmount();
        return rows.stream()
                .map(r -> new RecipientBudget(r.recipient(), r.groupName(), r.gifts(), r.total() / budget * 100))
                .collect(Collectors.toList());
    }

    // --------------------------------------------------------- secret santa
    public static List<Exclusion> exclusions() {
        return List.of(
                new Exclusion(1, MAYA, LEO, "Couple"),
                new Exclusion(2, JORDAN, NINA, "Already exchanging gifts")
        );
    }

    /** For each member: who they're allowed to draw (same group, not self, not excluded). */
    public static List<Eligibility> eligibility(Group g) {
        List<Exclusion> ex = exclusions();
        return g.members().stream().map(m -> {
            List<Member> allowed = g.members().stream()
                    .filter(o -> o.id() != m.id())
                    .filter(o -> ex.stream().noneMatch(e ->
                            (e.a().id() == m.id() && e.b().id() == o.id())
                                    || (e.b().id() == m.id() && e.a().id() == o.id())))
                    .collect(Collectors.toList());
            String names = allowed.stream().map(Member::firstName).collect(Collectors.joining(", "));
            return new Eligibility(m, allowed.size(), names);
        }).collect(Collectors.toList());
    }

    /** Who the current user drew. */
    public static Member santaRecipient() {
        return SAM;
    }
}
