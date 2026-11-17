package uga.cs4370.sleighlist.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import uga.cs4370.sleighlist.mock.MockData;
import uga.cs4370.sleighlist.models.Models;
import uga.cs4370.sleighlist.models.Models.Option;
import uga.cs4370.sleighlist.models.Models.Product;

/**
 * /products  - product browser (search by category, price range, rating)
 * /finder    - AI gift finder (Gemini turns a sentence into the same filters)
 */
@Controller
public class ProductController {

    @GetMapping("/products")
    public String products(@RequestParam(required = false) String q,
                           @RequestParam(required = false) String category,
                           @RequestParam(required = false) Double min,
                           @RequestParam(required = false) Double max,
                           @RequestParam(required = false) Double rating,
                           @RequestParam(required = false, defaultValue = "false") boolean bestseller,
                           @RequestParam(required = false, defaultValue = "popular") String sort,
                           @RequestParam(required = false, defaultValue = "1") int page,
                           Model model) {
        List<Product> results = MockData.searchProducts(q, category, min, max, rating, bestseller, sort);

        // Paging (backend: LIMIT ? OFFSET ? on the products query).
        int total = results.size();
        int pages = Math.max(1, (int) Math.ceil(total / (double) PAGE_SIZE));
        int current = Math.min(Math.max(page, 1), pages);
        int from = (current - 1) * PAGE_SIZE;
        int to = Math.min(from + PAGE_SIZE, total);
        List<Product> pageItems = results.subList(from, to);
        model.addAttribute("page", current);
        model.addAttribute("pages", pages);
        model.addAttribute("showPager", pages > 1);
        model.addAttribute("rangeLabel", total == 0 ? "0" : (from + 1) + " to " + to);
        model.addAttribute("prevUrl", current > 1 ? pageUrl(current - 1) : null);
        model.addAttribute("nextUrl", current < pages ? pageUrl(current + 1) : null);

        model.addAttribute("title", "Browse gifts");
        model.addAttribute("navProducts", true);
        model.addAttribute("q", q);
        model.addAttribute("min", min == null ? "" : trim(min));
        model.addAttribute("max", max == null ? "" : trim(max));
        model.addAttribute("bestseller", bestseller);
        model.addAttribute("categories", MockData.categories(category));
        model.addAttribute("ratings", List.of(
                new Option("", "Any rating", rating == null),
                new Option("4.5", "4.5 and up", Double.valueOf(4.5).equals(rating)),
                new Option("4", "4.0 and up", Double.valueOf(4.0).equals(rating)),
                new Option("3", "3.0 and up", Double.valueOf(3.0).equals(rating))));
        model.addAttribute("sorts", List.of(
                new Option("popular", "Most reviewed", "popular".equals(sort)),
                new Option("rating", "Highest rated", "rating".equals(sort)),
                new Option("price-asc", "Price: low to high", "price-asc".equals(sort)),
                new Option("price-desc", "Price: high to low", "price-desc".equals(sort))));
        model.addAttribute("products", pageItems);
        model.addAttribute("resultCount", total);
        model.addAttribute("catalogSize", Models.count(MockData.totalProducts()));
        model.addAttribute("myLists", MockData.myListOptions(0));
        return "products";
    }

    @GetMapping("/finder")
    public String finder(@RequestParam(required = false) String q,
                         @RequestParam(required = false, defaultValue = "0") int listId, Model model) {
        model.addAttribute("title", "Gift finder");
        model.addAttribute("navFinder", true);
        model.addAttribute("q", q);
        model.addAttribute("examples", List.of(
                "My dad likes fishing and grilling, around $50",
                "Cozy gift for my sister who's always cold, under $40",
                "Board game for roommates who love strategy",
                "Something for a coffee snob, $100 max"));
        model.addAttribute("myLists", MockData.myListOptions(listId));
        model.addAttribute("listId", listId);
        MockData.myLists(listId).stream().filter(l -> l.selected()).findFirst()
                .ifPresent(l -> model.addAttribute("targetList", l.name()));

        if (q != null && !q.isBlank()) {
            // Canned interpretation until the Gemini call exists. The real flow:
            // Gemini -> {categories, keywords, min, max} -> same search as /products.
            model.addAttribute("hasQuery", true);
            model.addAttribute("parsedCategories", List.of("Sports & Outdoors", "Patio, Lawn & Garden", "Kitchen & Dining"));
            model.addAttribute("parsedKeywords", List.of("fishing", "grill", "tackle", "thermometer"));
            model.addAttribute("parsedRange", "$15 to $65");
            model.addAttribute("browseLink", "/products?category=Sports+%26+Outdoors&min=15&max=65");
            model.addAttribute("products", List.of(
                    MockData.product(110), MockData.product(111), MockData.product(121),
                    MockData.product(122), MockData.product(106), MockData.product(108)));
        }
        return "finder";
    }

    private static final int PAGE_SIZE = 9;

    private static String pageUrl(int page) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .replaceQueryParam("page", page).build().toUriString();
    }

    private static String trim(double d) {
        return d == Math.floor(d) ? String.valueOf((long) d) : String.valueOf(d);
    }
}
