package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.entity.Address;
import com.dostavljaci.FoodDelivery.entity.MenuItem;
import com.dostavljaci.FoodDelivery.entity.Restaurant;
import com.dostavljaci.FoodDelivery.entity.User;
import com.dostavljaci.FoodDelivery.service.MenuItemService;
import com.dostavljaci.FoodDelivery.service.RestaurantService;
import com.dostavljaci.FoodDelivery.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Controller
@AllArgsConstructor
@RequestMapping("/menu-items")
public class MenuItemController {
    public final MenuItemService menuItemService;
    public final RestaurantService restaurantService;
    public final UserService userService;


    @GetMapping("/{restaurantName}")
    public String restaurantInfo(Model model,
                                 @PathVariable String restaurantName,
                                 HttpSession session){
        Restaurant restaurant=restaurantService.getRestaurantByName(restaurantName);
        System.out.print(restaurant);
        List<MenuItem> menuItem= menuItemService.getMenuByRestaurantId(restaurant.getId());

        model.addAttribute("restaurant",restaurant);
        model.addAttribute("menuItem", menuItem);

        return "restaurant-info";

    }

    @GetMapping("/add-item/{restaurantName}")
    public String showMenuItemForm(Model model, @PathVariable String restaurantName) {

        model.addAttribute("address", new MenuItem());
        model.addAttribute("restaurantName", restaurantName);
        model.addAttribute("error", null);
            return "add-menuitem";
    }

    @PostMapping("/add-item/{restaurantName}")
    public String handleMenuItemSubmission(@ModelAttribute MenuItem menuItem,
                                             Model model, HttpSession session,
                                             @PathVariable String restaurantName){

        Object sessionUser = session.getAttribute("user");
        Restaurant restaurant=restaurantService.getRestaurantByName(restaurantName);


        if (sessionUser instanceof User userInstance){
            if (Objects.equals(userService.getUserByUsername(userInstance.getUsername()).getRole().toLowerCase(), "admin")
                    || Objects.equals(userService.getUserById(userInstance.getId()),restaurant.getOwner()))
            {

                menuItem.setRestaurant(restaurant);

                menuItemService.saveMenuItem(menuItem);


                return "redirect:/profile/" + userInstance.getUsername();
            }
        }
        return "redirect:/";
    }

    @GetMapping("/{restaurantName}/edit-menuitem/{menuItemId}")
    public String showEditMenuItemForm(@PathVariable String restaurantName,
                                       @PathVariable UUID menuItemId, Model model, HttpSession session) {
        Object sessionUser = session.getAttribute("user");
        Restaurant restaurant = restaurantService.getRestaurantByName(restaurantName);

        if (sessionUser instanceof User userInstance) {
            MenuItem menuItem = menuItemService.getMenuItemById(menuItemId);


            if (menuItem != null &&
                    (Objects.equals(userService.getUserByUsername(userInstance.getUsername()).getRole().toLowerCase(), "admin")
                            || Objects.equals(userService.getUserById(userInstance.getId()), restaurant.getOwner()))) {

                model.addAttribute("menuItem", menuItem);
                model.addAttribute("restaurant",restaurant);
                model.addAttribute("error",null);
                return "edit-menuitem";
            }
        }
        return "redirect:/";
    }

    @PostMapping("/{restaurantName}/edit-menuitem/{menuItemId}")
    public String handleEditMenuItemSubmission(@ModelAttribute MenuItem menuItem,
                                               Model model, HttpSession session,
                                               @PathVariable String restaurantName,
                                               @PathVariable UUID menuItemId) {
        Object sessionUser = session.getAttribute("user");
        Restaurant restaurant = restaurantService.getRestaurantByName(restaurantName);

        if (sessionUser instanceof User userInstance) {
            if (Objects.equals(userService.getUserByUsername(userInstance.getUsername()).getRole().toLowerCase(), "admin")
                    || Objects.equals(userService.getUserById(userInstance.getId()), restaurant.getOwner())) {


                menuItem.setRestaurant(restaurant);


                menuItem.setId(menuItemId);


                menuItemService.saveMenuItem(menuItem);

                return "redirect:/menu-items/" + restaurantName;
            }
        }
        return "redirect:/";
    }

}
