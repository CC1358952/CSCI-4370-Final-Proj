package uga.cs4370.sleighlist.controllers;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Placeholder handlers for every form in the UI so clicking around never
 * hits a 405. Each one just redirects back with a toast.
 *
 * Backend team: this list is the endpoint contract the templates post to.
 * Move each mapping into its real controller (and run the matching prepared
 * statement) as you implement it, then delete it from here.
 */
@Controller
public class PrototypeActionController {

    // Wish lists
    @PostMapping("/lists")
    public String createList(HttpServletRequest req, RedirectAttributes ra) {
        return back(req, ra, "List created");
    }

    @PostMapping("/lists/items")
    public String addItem(HttpServletRequest req, RedirectAttributes ra) {
        return back(req, ra, "Added to your list");
    }

    @PostMapping("/lists/{listId}/items/{itemId}")
    public String updateItem(HttpServletRequest req, RedirectAttributes ra) {
        return back(req, ra, "Item updated");
    }

    @PostMapping("/lists/{listId}/items/{itemId}/delete")
    public String removeItem(HttpServletRequest req, RedirectAttributes ra) {
        return back(req, ra, "Item removed");
    }

    // Claims
    @PostMapping("/claims")
    public String claim(HttpServletRequest req, RedirectAttributes ra) {
        return back(req, ra, "Claimed. The list owner won't see this.");
    }

    @PostMapping("/claims/{claimId}/purchased")
    public String markPurchased(HttpServletRequest req, RedirectAttributes ra) {
        return back(req, ra, "Marked as purchased");
    }

    @PostMapping("/claims/{claimId}/delete")
    public String unclaim(HttpServletRequest req, RedirectAttributes ra) {
        return back(req, ra, "Claim released");
    }

    // Budget
    @PostMapping("/budget")
    public String updateBudget(HttpServletRequest req, RedirectAttributes ra) {
        return back(req, ra, "Budget updated");
    }

    // Groups
    @PostMapping("/groups")
    public String createGroup(HttpServletRequest req, RedirectAttributes ra) {
        return back(req, ra, "Group created");
    }

    @PostMapping("/groups/join")
    public String joinGroup(HttpServletRequest req, RedirectAttributes ra) {
        return back(req, ra, "Joined group");
    }

    @PostMapping("/groups/{groupId}/leave")
    public String leaveGroup(RedirectAttributes ra) {
        ra.addFlashAttribute("toast", "You left the group (prototype: nothing was saved)");
        return "redirect:/groups";
    }

    @PostMapping("/groups/{groupId}/santa/exclusions")
    public String addExclusion(HttpServletRequest req, RedirectAttributes ra) {
        return back(req, ra, "Exclusion added");
    }

    @PostMapping("/groups/{groupId}/santa/exclusions/{exclusionId}/delete")
    public String removeExclusion(HttpServletRequest req, RedirectAttributes ra) {
        return back(req, ra, "Exclusion removed");
    }

    private static String back(HttpServletRequest req, RedirectAttributes ra, String message) {
        ra.addFlashAttribute("toast", message + " (prototype: nothing was saved)");
        String referer = req.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/groups");
    }
}
