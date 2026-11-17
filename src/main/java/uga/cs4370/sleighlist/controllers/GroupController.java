package uga.cs4370.sleighlist.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import uga.cs4370.sleighlist.mock.MockData;
import uga.cs4370.sleighlist.models.Models;
import uga.cs4370.sleighlist.models.Models.ClaimTag;
import uga.cs4370.sleighlist.models.Models.Fact;
import uga.cs4370.sleighlist.models.Models.Group;
import uga.cs4370.sleighlist.models.Models.Member;
import uga.cs4370.sleighlist.models.Models.MemberStat;

/**
 * /groups                          - groups you belong to, create / join
 * /groups/{id}                     - group dashboard (stats)
 * /groups/{id}/members/{userId}    - a member's list with claims (spoiler-protected)
 * /groups/{id}/santa               - Secret Santa setup / reveal
 */
@Controller
public class GroupController {

    @GetMapping("/groups")
    public String groups(Model model) {
        model.addAttribute("title", "Your groups");
        model.addAttribute("navGroups", true);
        model.addAttribute("groups", MockData.groups());
        return "groups";
    }

    @GetMapping("/groups/{groupId}")
    public String dashboard(@PathVariable int groupId, Model model) {
        Group g = MockData.group(groupId);
        List<MemberStat> stats = MockData.memberStats(g);
        int totalItems = stats.stream().mapToInt(MemberStat::items).sum();
        double avg = stats.stream().mapToDouble(MemberStat::listValue).average().orElse(0);
        // Claimed % only counts other people's lists (your own claims are hidden from you).
        int othersItems = stats.stream().filter(s -> !s.you()).mapToInt(MemberStat::items).sum();
        int othersOpen = stats.stream().filter(s -> !s.you()).mapToInt(MemberStat::unclaimed).sum();

        model.addAttribute("title", g.name());
        model.addAttribute("navGroups", true);
        model.addAttribute("group", g);
        model.addAttribute("facts", List.of(
                new Fact(String.valueOf(g.memberCount()), "members"),
                new Fact(String.valueOf(totalItems), "items wished for"),
                new Fact((othersItems == 0 ? 0 : (othersItems - othersOpen) * 100 / othersItems) + "%", "of others' items claimed"),
                new Fact(Models.money(avg), "average list value")));
        model.addAttribute("stats", stats);
        model.addAttribute("waiting", stats.stream().filter(s -> !s.you() && s.unclaimed() > 0).toList());
        model.addAttribute("categories", MockData.topCategories());
        return "group_dashboard";
    }

    @GetMapping("/groups/{groupId}/members/{userId}")
    public String memberList(@PathVariable int groupId, @PathVariable int userId,
                             @RequestParam(required = false) String as, Model model) {
        if (userId == MockData.currentUser().id()) {
            return "redirect:/lists";
        }
        Group g = MockData.group(groupId);
        Member owner = MockData.member(userId);
        List<ClaimTag> tags = MockData.friendList(g, owner);
        boolean ownerView = "owner".equals(as);

        long claimed = tags.stream().filter(t -> !t.unclaimed()).count();
        List<ClaimTag> mine = tags.stream().filter(ClaimTag::claimedByYou).toList();
        double mineTotal = mine.stream().mapToDouble(t -> t.product().price()).sum();

        model.addAttribute("title", owner.firstName() + "'s list");
        model.addAttribute("navGroups", true);
        model.addAttribute("group", g);
        model.addAttribute("owner", owner);
        model.addAttribute("listName", g.id() == 1 ? "Christmas 2026" : g.occasion() + " list");
        model.addAttribute("ownerView", ownerView);
        model.addAttribute("tags", tags);
        model.addAttribute("itemCount", tags.size());
        model.addAttribute("claimedCount", claimed);
        model.addAttribute("openCount", tags.size() - claimed);
        model.addAttribute("mineCount", mine.size());
        model.addAttribute("mineTotal", Models.money(mineTotal));
        model.addAttribute("otherMembers", g.members().stream()
                .filter(m -> m.id() != MockData.currentUser().id() && m.id() != owner.id()).toList());
        return "friend_list";
    }

    @GetMapping("/groups/{groupId}/santa")
    public String santa(@PathVariable int groupId,
                        @RequestParam(required = false) Boolean drawn, Model model) {
        Group g = MockData.group(groupId);
        boolean isDrawn = drawn != null ? drawn : g.santaDrawn();

        model.addAttribute("title", "Secret Santa");
        model.addAttribute("navGroups", true);
        model.addAttribute("group", g);
        model.addAttribute("drawn", isDrawn);
        model.addAttribute("recipient", MockData.santaRecipient(g));
        model.addAttribute("exclusions", MockData.exclusions(g));
        model.addAttribute("eligibility", MockData.eligibility(g));
        model.addAttribute("members", g.members());
        return "santa";
    }

    /** Prototype only: pretend the draw ran and show the result. */
    @PostMapping("/groups/{groupId}/santa/draw")
    public String draw(@PathVariable int groupId) {
        return "redirect:/groups/" + groupId + "/santa?drawn=true";
    }

    @PostMapping("/groups/{groupId}/santa/reset")
    public String reset(@PathVariable int groupId) {
        return "redirect:/groups/" + groupId + "/santa?drawn=false";
    }
}
