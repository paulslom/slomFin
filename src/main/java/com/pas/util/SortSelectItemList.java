package com.pas.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jakarta.faces.model.SelectItem;

public class SortSelectItemList 
{
    public static void sortSelectItemList(List<SelectItem> selectItems, String sortBy, boolean ascending) 
    {
        Collections.sort(selectItems, new Comparator<SelectItem>() 
        {
            @Override
            public int compare(SelectItem item1, SelectItem item2) 
            {
                Comparable value1 = getComparableValue(item1, sortBy);
                Comparable value2 = getComparableValue(item2, sortBy);

                int comparisonResult = value1.compareTo(value2);

                return ascending ? comparisonResult : -comparisonResult;
            }
        });
    }

    private static Comparable getComparableValue(SelectItem item, String sortBy) 
    {
        switch (sortBy) 
        {
            case "label":
                return item.getLabel();
                
            case "value":
                return (Comparable) item.getValue();
                
            // Add more cases for other properties as needed
            default:
                throw new IllegalArgumentException("Invalid sortBy property: " + sortBy);
        }
    }

    public static void main(String[] args) 
    {
        // Example usage
        List<SelectItem> items = List.of(
                new SelectItem("value3", "Label C"),
                new SelectItem("value1", "Label A"),
                new SelectItem("value2", "Label B")
        );
        
        System.out.println("Before sorting:");
        items.forEach(item -> System.out.println(item.getLabel()));

        sortSelectItemList(items, "label", true);

       System.out.println("\nAfter sorting by label (ascending):");
        items.forEach(item -> System.out.println(item.getLabel()));

        sortSelectItemList(items, "value", false);

        System.out.println("\nAfter sorting by value (descending):");
        items.forEach(item -> System.out.println(item.getValue()));
    }
}
