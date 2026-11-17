package uga.cs4370.sleighlist.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import uga.cs4370.sleighlist.mock.MockData;
import uga.cs4370.sleighlist.models.Models.WishList;

/** /lists and /lists/{id} - the current user's own wish lists. */
@Controller
public class WishListController {

    @GetMapping("/lists")
    public String lists(Model model) {
        return list(1, model);
    }

    @GetMapping("/lists/{listId}")
    public String list(@PathVariable int listId, Model model) {
        List<WishList> lists = MockData.myLists(listId);
        WishList selected = lists.stream().filter(WishList::selected).findFirst().orElse(lists.get(0));

        model.addAttribute("title", "My wish lists");
        model.addAttribute("navLists", true);
        model.addAttribute("lists", lists);
        model.addAttribute("list", selected);
        model.addAttribute("groups", MockData.groups());
        return "lists";
    }
}
