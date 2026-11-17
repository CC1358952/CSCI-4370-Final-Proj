package uga.cs4370.sleighlist.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import uga.cs4370.sleighlist.mock.MockData;
import uga.cs4370.sleighlist.models.Models.Member;

/** Values every page's nav needs. Swap MockData for the session user later. */
@ControllerAdvice
public class GlobalModel {

    @ModelAttribute("me")
    public Member me() {
        return MockData.currentUser();
    }
}
