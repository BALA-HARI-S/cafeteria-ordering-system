package net.breezeware.service.impl;


import net.breezeware.dataStore.FoodItemDataStore;
import net.breezeware.entity.FoodItem;
import net.breezeware.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class FoodItemManagerTest {

//    @InjectMocks
    @Mock
    FoodItemDataStore foodItemDataStore = Mockito.mock(FoodItemDataStore.class);

    @InjectMocks
    FoodItemManagerImplementation foodItemManagerImplementation = new FoodItemManagerImplementation(foodItemDataStore);

    // Creating Food Items by providing Empty Food Item Name Throws Exception
    @Test
    void givenEmptyFoodItemName_WhenFoodItemNameIsEmpty_ThenThrowsException(){
        CustomException customException = org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () -> {
            foodItemManagerImplementation.createFoodItem("", 10);
        });
        Assertions.assertThat(customException.getMessage()).isEqualTo("Food Item Name Cannot be Empty and should only contains Letters!");
    }

    // Creating Food Items by providing Integer to Food Item Name Throws Exception
    @Test
    void givenEmptyFoodItemName_WhenFoodItemNameIsInteger_ThenThrowsException(){
        CustomException customException = org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () -> {
            foodItemManagerImplementation.createFoodItem("123", 10);
        });
        Assertions.assertThat(customException.getMessage()).isEqualTo("Food Item Name Cannot be Empty and should only contains Letters!");
    }

    // Creating Food Items by providing Valid Food Item Details
    @Test
    void givenValidFoodItemDetails_WhenFoodItemAdded_ThenSuccessResponse() throws CustomException {
        // Stubbing the behavior of insertFoodItem method to return a dummy FoodItem
        when(foodItemDataStore.insertFoodItem(anyString(), anyInt(), anyDouble(), any(), any()))
                .thenReturn(new FoodItem(1, "Test", 10));

        // Calling the method under test
        FoodItem foodItem = foodItemManagerImplementation.createFoodItem("Test", 10);

        // Assertions or further verifications can go here
        Assertions.assertThat(foodItem.getName()).isEqualTo("Test");
    }

    // Retrieve Food Item by providing Invalid Food Item Name
    @Test
    void givenInvalidFoodItemName_WhenInvalidFoodItemName_ThenThrowsException() throws CustomException {
        when(foodItemDataStore.queryFoodItem(anyString())).thenThrow(new CustomException("Food Item not found for name: invalidName"));
        CustomException customException = org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () ->
                foodItemManagerImplementation.retrieveFoodItem("invalidName"));
        Assertions.assertThat(customException.getMessage()).isEqualTo("Food Item not found for name: invalidName");
    }

    // Retrieve Food Item by providing a valid Food Item Name
    @Test
    void givenValidFoodItemName_WhenValidFoodItemName_ThenSuccessResponse() throws CustomException {
        when(foodItemDataStore.queryFoodItem(anyString())).thenReturn(new FoodItem(1,"Juice",10));
        FoodItem foodItem = foodItemManagerImplementation.retrieveFoodItem("Juice");
        Assertions.assertThat(foodItem.getName()).isEqualTo("Juice");
    }


    // Retrieve All Food Items When Nothing's in Food Items Table
    @Test
    void givenRetrieveAllFoodItemsCall_WhenRetrieveAllFoodItemsListIsEmpty_ThenThrowsException() throws CustomException {
        when(foodItemDataStore.queryAllFoodItems(true,1,"_id"))
                .thenThrow(new CustomException("Food Items Storage is Empty!"));
        CustomException customException = org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () ->
                foodItemManagerImplementation.retrieveAllFoodItems(true,1,"_id"));
        Assertions.assertThat(customException.getMessage()).isEqualTo("Food Items Storage is Empty!");
    }

    // Retrieve All Food Items If Exists
    @Test
    void givenRetrieveAllFoodItemsCall_WhenRetrieveAllFoodItems_ThenSuccessResponse() throws CustomException {
        List<FoodItem> foodItems = Mockito.mock(ArrayList.class);
        when(foodItemDataStore.queryAllFoodItems(true,1,"_id")).thenReturn(foodItems);
        Assertions.assertThatCollection(foodItemManagerImplementation
                .retrieveAllFoodItems(true,1,"_id")).isEqualTo(foodItems);
    }

    // Updating Exist Food Item Name by providing Invalid Food Item Names or Empty Names
    @Test
    void givenNewFoodItemNameToUpdateExistingFoodItemName_WhenInvalidFoodItemName_ThenThrowsException() throws CustomException {
        CustomException emptyNamesException = org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () ->
                foodItemManagerImplementation.updateFoodItemName("", "123food"));
        Assertions.assertThat(emptyNamesException.getMessage()).isEqualTo("Food Item Name Cannot be Empty and should only contains Letters!");
    }

    // Deleting Food Item That doesn't exist
    @Test
    void givenDeleteFoodItem_whenFoodItemNotExist_ThenReturnFalse() throws CustomException {
        when(foodItemDataStore.deleteFoodItem("Test")).thenReturn(false);
        Assertions.assertThat(foodItemManagerImplementation.deleteFoodItem("Test")).isEqualTo(false);
    }

    // Delete Food Item If Exist
    @Test
    void givenDeleteFoodItemIfExist_whenFoodItemExitDelete_ThenSuccessResponse() throws CustomException {
        when(foodItemDataStore.deleteFoodItem("Test")).thenReturn(true);
        Assertions.assertThat(foodItemManagerImplementation.deleteFoodItem("Test")).isEqualTo(true);
    }
}

