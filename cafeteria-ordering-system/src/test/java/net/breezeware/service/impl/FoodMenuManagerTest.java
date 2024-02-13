package net.breezeware.service.impl;

import net.breezeware.dataStore.FoodMenuDataStore;
import net.breezeware.entity.MenuAvailability;
import net.breezeware.entity.FoodItem;
import net.breezeware.entity.FoodMenu;
import net.breezeware.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class FoodMenuManagerTest {


    @Mock
    private FoodMenuDataStore foodMenuDataStore;

    @InjectMocks
    private FoodMenuManagerImplementation foodMenuManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Create Food by providing Empty Food Menu Name
    @Test
    void givenEmptyFoodMenuName_WhenCreateFoodMenu_ThenThrowsException() throws  Exception {
        List<MenuAvailability> availableDays = new ArrayList<>();
        availableDays.add(MenuAvailability.MONDAY);
        Assertions.assertThatThrownBy(() -> foodMenuManager.createFoodMenu("",availableDays))
                .isInstanceOf(CustomException.class)
                .hasMessage("Food Item Menu Cannot be Empty and should only contains a-z,A-Z,0-9");
    }

    // Create Food by providing Invalid Food Menu Name
    @Test
    void givenInvalidFoodMenuName_WhenCreateFoodMenu_ThenThrowsException() throws  Exception {
        List<MenuAvailability> availableDays = new ArrayList<>();
        availableDays.add(MenuAvailability.MONDAY);
        Assertions.assertThatThrownBy(() -> foodMenuManager.createFoodMenu("food-menu",availableDays))
                .isInstanceOf(CustomException.class)
                .hasMessage("Food Item Menu Cannot be Empty and should only contains a-z,A-Z,0-9");
    }

    // Create Food Menu
    @Test
    void givenValidFoodMenuDetails_WhenCreateFoodMenu_ThenReturnFoodMenu() throws CustomException {
        String name = "Test Menu";
        List<MenuAvailability> availableDays = new ArrayList<>();
        availableDays.add(MenuAvailability.MONDAY);

        // Mocking Instant.now() to return a fixed value
        Instant fixedInstant = Instant.parse("2024-02-12T10:15:30.00Z");

        when(foodMenuDataStore.insertFoodMenu(anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new FoodMenu(name,availableDays,fixedInstant,fixedInstant));

        FoodMenu result = foodMenuManager.createFoodMenu(name, availableDays);
        Assertions.assertThat(result.getName()).isEqualTo(name);
    }

    // Retrieve Food Menu of the Day when No Menu's are Available for the particular Day
    @Test
    void givenRetrieveNonExistFoodMenuFromMenuList_WhenRetrieveFoodMenuOfTheDay_ThenThrowsException() throws CustomException {
        when(foodMenuDataStore.queryAllFoodMenu(true,1,"_id")).thenReturn(new ArrayList<FoodMenu>());
        Assertions.assertThatThrownBy(() -> foodMenuManager.retrieveFoodMenuOfTheDay())
                .isInstanceOf(CustomException.class)
                .hasMessage("No Food Menu Available Today!");
    }

    // Retrieve Food Menu of the Day
    @Test
    void givenRetrieveFoodMenuOfTheDayFromMenuList_WhenRetrieveFoodMenuOfTheDay_ThenReturnFoodMenu() throws CustomException {
        String name = "Test Menu";
        List<MenuAvailability> availableDays = new ArrayList<>();
        availableDays.add(MenuAvailability.MONDAY);
        availableDays.add(MenuAvailability.TUESDAY);
        availableDays.add(MenuAvailability.WEDNESDAY);
        availableDays.add(MenuAvailability.THURSDAY);
        availableDays.add(MenuAvailability.FRIDAY);
        availableDays.add(MenuAvailability.SATURDAY);
        availableDays.add(MenuAvailability.SUNDAY);

        // Mocking Instant.now() to return a fixed value
        Instant fixedInstant = Instant.parse("2024-02-12T10:15:30.00Z");

        List<FoodMenu> foodMenuList = new ArrayList<>();
        foodMenuList.add(new FoodMenu(name,availableDays,fixedInstant,fixedInstant));

        when(foodMenuDataStore.queryAllFoodMenu(true,1,"_id")).thenReturn(foodMenuList);
        List<FoodMenu> foodMenus = foodMenuManager.retrieveFoodMenuOfTheDay();

        Assertions.assertThat(foodMenus.size()).isEqualTo(1);
        Assertions.assertThat(foodMenus.get(0).getName()).isEqualTo(name);
    }

    // Retrieve Food Menu by providing Empty Food Menu Name
    @Test
    void givenEmptyFoodMenuName_WhenRetrieveFoodMenu_ThenThrowsException() throws CustomException{
        Assertions.assertThatThrownBy(() -> foodMenuManager.retrieveFoodMenu(""))
                .isInstanceOf(CustomException.class)
                        .hasMessage("Food Item Menu Cannot be Empty and should only contains a-z,A-Z,0-9");
    }

    // Retrieve Food Menu by providing Invalid Food Menu Name
    @Test
    void givenInvalidFoodMenuName_WhenRetrieveFoodMenu_ThenThrowsException() throws CustomException{
        Assertions.assertThatThrownBy(() -> foodMenuManager.retrieveFoodMenu("food-menu-1"))
                .isInstanceOf(CustomException.class)
                .hasMessage("Food Item Menu Cannot be Empty and should only contains a-z,A-Z,0-9");
    }

    // Retrieve Non-Existing Food Menu
    @Test
    void givenFoodMenuName_WhenRetrieveFoodMenu_ThenThrowsException() throws CustomException{
        String foodMenuName = "Food Menu";
        when(foodMenuDataStore.queryFoodMenu(anyString())).thenThrow(new CustomException("Food Menu not found for name: " + foodMenuName));
        Assertions.assertThatThrownBy(() -> foodMenuManager.retrieveFoodMenu(foodMenuName))
                .isInstanceOf(CustomException.class)
                .hasMessage("Food Menu not found for name: " + foodMenuName);
    }

    // Retrieve Food Menu
    @Test
    void givenFoodMenuName_WhenRetrieveFoodMenu_ThenReturnFoodMenu() throws CustomException{
        String name = "Test Menu";
        List<MenuAvailability> availableDays = new ArrayList<>();
        availableDays.add(MenuAvailability.MONDAY);
        Instant fixedInstant = Instant.parse("2024-02-12T10:15:30.00Z");
        when(foodMenuDataStore.queryFoodMenu(anyString())).thenReturn(new FoodMenu(name,availableDays,fixedInstant,fixedInstant));
        FoodMenu foodMenu = foodMenuManager.retrieveFoodMenu(name);
        Assertions.assertThat(foodMenu.getName()).isEqualTo(name);
    }

    // Retrieve Food Menu Items -Throws Exception when there is no Items in the Menu
    @Test
    void givenFoodMenuId_WhenRetrieveFoodMenuItems_ThenThrowsException() throws CustomException{
        when(foodMenuDataStore.queryFoodMenuItems(1)).thenThrow(new CustomException("There are no Items in the Menu"));
        Assertions.assertThatThrownBy(() -> foodMenuManager.retrieveFoodMenuItems(1))
                .isInstanceOf(CustomException.class)
                .hasMessage("There are no Items in the Menu");
    }

    // Retrieve Food Menu Items - Food Menu Items List
    @Test
    void givenFoodMenuId_WhenRetrieveFoodMenuItems_ThenReturnFoodMenuItemsList() throws CustomException{
        List<FoodItem> foodMenuItems = new ArrayList<>();
        foodMenuItems.add(new FoodItem(1,"Food Item",10));

        when(foodMenuDataStore.queryFoodMenuItems(anyInt())).thenReturn(foodMenuItems);
        List<FoodItem> foodItems = foodMenuManager.retrieveFoodMenuItems(1);

        Assertions.assertThat(foodItems.size()).isEqualTo(1);
        Assertions.assertThat(foodItems.get(0).getName()).isEqualTo("Food Item");
    }

    // Retrieve Non-Exciting Food Menu Item - Throws Exception
    @Test
    void givenFoodMenuIdFoodItemId_WhenRetrieveFoodMenuItem_ThenThrowsException() throws CustomException{
        when(foodMenuDataStore.queryFoodMenuItem(anyInt(),anyInt())).thenThrow(new CustomException("Food Item Not in the Menu."));
        Assertions.assertThatThrownBy(() -> foodMenuManager.retrieveFoodMenuItem(1,1))
                .isInstanceOf(CustomException.class)
                .hasMessage("Food Item Not in the Menu.");
    }

    // Retrieve Food Menu Item
    @Test
    void givenFoodMenuIdFoodItemId_WhenRetrieveFoodMenuItem_ThenReturnFoodMenuFoodItem() throws CustomException{
        when(foodMenuDataStore.queryFoodMenuItem(anyInt(),anyInt())).thenReturn(new FoodItem(1,"Food Item",10));
        FoodItem foodItem = foodMenuManager.retrieveFoodMenuItem(1,1);
        Assertions.assertThat(foodItem.getName()).isEqualTo("Food Item");
    }

    // Retrieve All Food Menus - When Empty
    @Test
    void givenRetrieveFoodMenus_WhenRetrieveAllFoodMenus_ThenThrowsException() throws CustomException{
        when(foodMenuDataStore.queryAllFoodMenu(true,1,"_id")).thenThrow(new CustomException("Food Menu Storage is Empty!"));
        Assertions.assertThatThrownBy(() -> foodMenuManager.retrieveAllFoodMenus(true,1,"_id"))
                .isInstanceOf(CustomException.class)
                .hasMessage("Food Menu Storage is Empty!");
    }

    // Retrieve All Food Menus
    @Test
    void givenRetrieveFoodMenus_WhenRetrieveAllFoodMenus_ThenReturnFoodMenusList() throws CustomException{
        String name = "Test Menu";
        List<MenuAvailability> availableDays = new ArrayList<>();
        availableDays.add(MenuAvailability.MONDAY);
        Instant fixedInstant = Instant.parse("2024-02-12T10:15:30.00Z");
        List<FoodMenu> foodMenuList = new ArrayList<>();
        foodMenuList.add(new FoodMenu(name,availableDays,fixedInstant,fixedInstant));

        when(foodMenuDataStore.queryAllFoodMenu(true,1,"_id")).thenReturn(foodMenuList);

        List<FoodMenu> foodMenus = foodMenuManager.retrieveAllFoodMenus(true,1,"_id");
        Assertions.assertThat(foodMenus.size()).isEqualTo(1);
        Assertions.assertThat(foodMenus.get(0).getName()).isEqualTo(name);
    }

    // Add Food Item To Menu
    @Test
    void givenFoodMenuIdFoodItemId_WhenAddFoodItemsToMenu_ThenReturnTrue() throws CustomException{
        when(foodMenuDataStore.addFoodItemsToMenu(anyInt(),anyInt())).thenReturn(true);
        boolean isFoodItemAddedToMenu = foodMenuManager.addFoodItemsToMenu(1,1);
        Assertions.assertThat(isFoodItemAddedToMenu).isTrue();
    }

    // Delete Food Item From Menu
    @Test
    void givenFoodMenuIdFoodItemId_WhenDeleteFoodItemFromMenu_ThenReturnTrue() throws CustomException{
        when(foodMenuDataStore.deleteFoodItemFromMenu(anyInt(),anyInt())).thenReturn(true);
        boolean isFoodItemDeletedFromMenu = foodMenuManager.deleteFoodItemFromMenu(1,1);
        Assertions.assertThat(isFoodItemDeletedFromMenu).isTrue();
    }

    // Delete All Food Items From Menu
    @Test
    void givenFoodMenuId_WhenDeleteAllFoodItemsFromMenu_ThenReturnTrue() throws CustomException{
        when(foodMenuDataStore.deleteAllFoodItemsFromMenu(anyInt())).thenReturn(true);
        boolean isFoodItemsDeletedFromMenu = foodMenuManager.deleteAllFoodItemsFromMenu(1);
        Assertions.assertThat(isFoodItemsDeletedFromMenu).isTrue();
    }

    // Update Food Menu Name by providing Empty or Invalid Food Menu Name
    @Test
    void givenEmptyFoodMenuName_WhenUpdateFoodMenuName_ThenThrowsException() throws CustomException{
        Assertions.assertThatThrownBy(() -> foodMenuManager.updateFoodMenuName("","Food-Menu"))
                .isInstanceOf(CustomException.class)
                .hasMessage("Food Item Menu Cannot be Empty and should only contains a-z,A-Z,0-9");
    }

    // Update Food Menu Name
    @Test
    void givenExistingFoodMenuNameAndNewFoodMenuName_WhenUpdateFoodMenuName_ThenFoodMenu() throws CustomException{
        String name = "Test Menu";
        List<MenuAvailability> availableDays = new ArrayList<>();
        availableDays.add(MenuAvailability.MONDAY);
        Instant fixedInstant = Instant.parse("2024-02-12T10:15:30.00Z");

        when(foodMenuDataStore.updateFoodMenuName(anyString(),anyString()))
                .thenReturn(new FoodMenu(name,availableDays,fixedInstant,fixedInstant));

        FoodMenu updatedFoodMenu = foodMenuManager.updateFoodMenuName(name,"Some Menu");
        Assertions.assertThat(updatedFoodMenu.getName()).isEqualTo(name);
    }

    // Update Food Menu Available Day
    @Test
    void givenFoodItemAvailableDaysList_WhenUpdateFoodMenuAvailableDay_ThenReturnFoodMenu() throws CustomException{
        String name = "Test Menu";
        List<MenuAvailability> availableDays = new ArrayList<>();
        availableDays.add(MenuAvailability.MONDAY);
        Instant fixedInstant = Instant.parse("2024-02-12T10:15:30.00Z");

        when(foodMenuDataStore.updateFoodMenuAvailableDay(anyString(),anyString()))
                .thenReturn(new FoodMenu(name,availableDays,fixedInstant,fixedInstant));

        FoodMenu updatedFoodMenu = foodMenuManager.updateFoodMenuAvailableDay(availableDays,name);
        Assertions.assertThat(updatedFoodMenu.getAvailableDay().get(0)).isEqualTo(MenuAvailability.MONDAY);
    }

    // Delete Food Menu
    @Test
    void givenFoodMenuName_WhenDeleteFoodMenu_ThenReturnTrue() throws CustomException{
        String name = "Test Menu";
        List<MenuAvailability> availableDays = new ArrayList<>();
        availableDays.add(MenuAvailability.MONDAY);
        Instant fixedInstant = Instant.parse("2024-02-12T10:15:30.00Z");
        when(foodMenuDataStore.queryFoodMenu(anyString())).thenReturn(new FoodMenu(name,availableDays,fixedInstant,fixedInstant));
        when(foodMenuDataStore.deleteFoodMenu(anyString())).thenReturn(true);
        boolean isFoodMenuDeletedFromMenu = foodMenuManager.deleteFoodMenu(name);
        Assertions.assertThat(isFoodMenuDeletedFromMenu).isTrue();
    }

}