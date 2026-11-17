package uga.cs4370.sleighlist.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import uga.cs4370.sleighlist.mock.MockData;
import uga.cs4370.sleighlist.models.Models;
import uga.cs4370.sleighlist.models.Models.RecipientBudget;

/** /budget - everything you've claimed, totaled per recipient, against your budget. */
@Controller
public class BudgetController {

    @GetMapping("/budget")
    public String budget(Model model) {
        double budget = MockData.budgetAmount();
        List<RecipientBudget> rows = MockData.budgetByRecipient();
        double spent = rows.stream().mapToDouble(RecipientBudget::total).sum();
        double purchased = rows.stream()
                .flatMap(r -> r.gifts().stream())
                .filter(g -> g.purchased())
                .mapToDouble(g -> g.product().price()).sum();
        int giftCount = rows.stream().mapToInt(RecipientBudget::giftCount).sum();
        boolean over = spent > budget;

        model.addAttribute("title", "Budget");
        model.addAttribute("navBudget", true);
        model.addAttribute("budget", String.format("%.2f", budget));
        model.addAttribute("budgetLabel", Models.money(budget));
        model.addAttribute("spentLabel", Models.money(spent));
        model.addAttribute("purchasedLabel", Models.money(purchased));
        model.addAttribute("remainingLabel", Models.money(Math.abs(budget - spent)));
        model.addAttribute("over", over);
        model.addAttribute("giftCount", giftCount);
        model.addAttribute("recipientCount", rows.size());
        model.addAttribute("rows", rows);
        return "budget";
    }
}
