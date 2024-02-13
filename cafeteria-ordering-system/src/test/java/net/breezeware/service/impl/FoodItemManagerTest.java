package net.breezeware.service.impl;


import net.breezeware.dataStore.FoodItemDataStore;
import net.breezeware.entity.FoodItem;
import net.breezeware.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class FoodItemManagerTest {


    @Mock
    private FoodItemDataStore foodItemDataStore;

    @InjectMocks
    private FoodItemManagerImplementation foodItemManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Creating Food Item by providing Empty Food Item Name Throws Exception
    @Test
    void givenEmptyFoodItemName_WhenCreateFoodItem_ThenThrowsException(){
        Assertions.assertThatThrownBy(() -> foodItemManager.createFoodItem("", 10.00))
                .isInstanceOf(CustomException.class)
                .hasMessage("Food Item Name Cannot be Empty and should only contains Letters!");
    }

    // Creating Food Item by providing Invalid to Food Item Name Throws Exception
    @Test
    void givenInvalidFoodItemName_WhenCreateFoodItem_ThenThrowsException(){
        Assertions.assertThatThrownBy(() -> foodItemManager.createFoodItem("Food-1", 10.00))
                .isInstanceOf(CustomException.class)
                .hasMessage("Food Item Name Cannot be Empty and should only contains Letters!");
    }

    // Create Food Item
    @Test
    void givenValidFoodItemDetails_WhenCreateFoodItem_ThenReturnFoodItem() throws CustomException {
        // Stubbing the behavior of insertFoodItem method to return a dummy FoodItem
        when(foodItemDataStore.insertFoodItem(anyString(), anyInt(), anyDouble(), any(), any()))
                .thenReturn(new FoodItem(1, "Test", 10));

        // Calling the method under test
        FoodItem foodItem = foodItemManager.createFoodItem("Test", 10);

        // Assertions
        Assertions.assertThat(foodItem.getName()).isEqualTo("Test");
    }

    // Retrieve Food Item by providing Invalid Food Item Name
    @Test
    void givenInvalidFoodItemName_WhenRetrieveFoodItem_ThenThrowsException() throws CustomException {
        Assertions.assertThatThrownBy(() -> foodItemManager.retrieveFoodItem("Food-1"))
                .isInstanceOf(CustomException.class)
                .hasMessage("Food Item Name Cannot be Empty and should only contains Letters!");
    }

    // Retrieve Food Item
    @Test
    void givenValidFoodItemNameName_WhenRetrieveFoodItem_ThenReturnFoodItem() throws CustomException {
        when(foodItemDataStore.queryFoodItem(anyString())).thenReturn(new FoodItem(1,"Juice",10));
        FoodItem foodItem = foodItemManager.retrieveFoodItem("Juice");
        Assertions.assertThat(foodItem.getName()).isEqualTo("Juice");
    }


    // Retrieve All Food Items When Nothing's in Food Items Table
    @Test
    void givenRetrieveAllFoodItemsOrderById_WhenRetrieveAllFoods_ThenThrowsException() throws CustomException {
        when(foodItemDataStore.queryAllFoodItems(anyBoolean(),anyInt(),anyString()))
                .thenThrow(new CustomException("Food Items Storage is Empty!"));
        Assertions.assertThatThrownBy(() -> foodItemManager.retrieveAllFoodItems(true,1,"_id"))
                .isInstanceOf(CustomException.class)
                .hasMessage("Food Items Storage is Empty!");
    }

    // Retrieve All Food Items
    @Test
    void givenRetrieveAllFoodItemsOrderById_WhenRetrieveAllFoodItems_ThenReturnFoodItemsList() throws CustomException {
        List<FoodItem> foodItems = Mockito.mock(ArrayList.class);
        when(foodItemDataStore.queryAllFoodItems(true,1,"_id")).thenReturn(foodItems);
        Assertions.assertThatCollection(foodItemManager
                .retrieveAllFoodItems(true,1,"_id")).isEqualTo(foodItems);
    }

    // Updating Exist Food Item Name by providing Invalid Food Item Name or Empty Name
    @Test
    void givenInvalidFoodItemName_WhenUpdateFoodItemName_ThenThrowsException() throws CustomException {
        CustomException emptyNamesException = org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () ->
                foodItemManager.updateFoodItemName("", "123food"));
        Assertions.assertThat(emptyNamesException.getMessage()).isEqualTo("Food Item Name Cannot be Empty and should only contains Letters!");
    }

    // Updating Exist Food Item Name by providing Valid Food Item Name
    @Test
    void givenNewFoodItemName_WhenUpdateFoodItemName_ThenThrowsException() throws CustomException {
        when(foodItemDataStore.updateFoodItemName(anyString(), anyString())).thenReturn(new FoodItem(1, "Egg Rice", 1));
        FoodItem foodItem =  foodItemManager.updateFoodItemName("Egg Rice", "Chicken Rice");
        Assertions.assertThat("Egg Rice").isEqualTo(foodItem.getName());
    }

    // Updating Food Item Quantity of Non-Existing Food Item
    @Test
    void givenNewFoodItemQuantityForNonExistingItem_WhenUpdateFoodItemQuantity_ThenThrowsException() throws CustomException {
        when(foodItemDataStore.updateFoodItemQuantity(anyInt(),anyString())).thenThrow(new CustomException("Food Item not available"));
        Assertions.assertThatThrownBy(() -> foodItemManager.updateFoodItemQuantity(10,"Rice"))
                .isInstanceOf(CustomException.class).hasMessage("Food Item not available");
    }

    //Updating Food Item Quantity
    @Test
    void givenNewFoodItemQuantity_WhenUpdateFoodItemQuantity_ThenThrowsException() throws CustomException {
        when(foodItemDataStore.queryFoodItem(anyString())).thenReturn(new FoodItem(1,"Rice",5));
        when(foodItemDataStore.updateFoodItemQuantity(anyInt(),anyString())).thenReturn(new FoodItem(1,"Rice",10));
        FoodItem foodItem = foodItemManager.updateFoodItemQuantity(10,"Rice");
        Assertions.assertThat(foodItem.getQuantity()).isEqualTo(10);
    }

    //Updating Food Item Price of Non-Existing Food Item
    @Test
    void givenNewFoodItemPriceForNonExistingItem_WhenUpdateFoodItemPrice_ThenThrowsException() throws CustomException {
        when(foodItemDataStore.updateFoodItemPrice(anyDouble(),anyString())).thenThrow(new CustomException("Couldn't Update Food Item Price"));
        Assertions.assertThatThrownBy(() -> foodItemManager.updateFoodItemPrice(10.00,"Rice"))
                .isInstanceOf(CustomException.class).hasMessage("Couldn't Update Food Item Price");
    }

    // Updating Food Item Price
    @Test
    void givenNewFoodItemPrice_WhenUpdateFoodItemPrice_ThenThrowsException() throws CustomException {
        when(foodItemDataStore.updateFoodItemPrice(anyDouble(),anyString()))
                .thenReturn(new FoodItem("Rice",5,15.00, Instant.now(),Instant.now()));
        FoodItem foodItem = foodItemManager.updateFoodItemPrice(15.00,"Rice");
        Assertions.assertThat(foodItem.getPrice()).isEqualTo(15.00);
    }

    // Deleting Food Item That doesn't exist
    @Test
    void givenFoodItemName_whenDeleteFoodItem_ThenReturnFalse() throws CustomException {
        when(foodItemDataStore.deleteFoodItem("Test")).thenReturn(false);
        Assertions.assertThat(foodItemManager.deleteFoodItem("Test")).isEqualTo(false);
    }

    // Delete Food Item If Exist
    @Test
    void givenFoodItemName_whenDeleteFoodItem_ThenSuccessResponse() throws CustomException {
        when(foodItemDataStore.deleteFoodItem("Test")).thenReturn(true);
        Assertions.assertThat(foodItemManager.deleteFoodItem("Test")).isEqualTo(true);
    }
}

