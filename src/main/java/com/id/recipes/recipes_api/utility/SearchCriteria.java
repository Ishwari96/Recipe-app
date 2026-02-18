package com.id.recipes.recipes_api.utility;

import java.util.List;

public record SearchCriteria(
        Boolean vegetarian,
        Integer servings,
        List<String> include,
        List<String> exclude,
        String instructionsContains,
        String instructionsNotContains
) {
    /** Returns true when there are no filters provided*/
    public boolean isEmpty() {
        boolean vegetarianInactive = (vegetarian == null) || !Boolean.TRUE.equals(vegetarian);
        boolean servingsInactive = (servings == null) || servings <= 0;
        boolean includeInactive = include == null || include.isEmpty();
        includeInactive = isIncludeExcludeInactive(includeInactive);
        boolean excludeInactive = exclude == null || exclude.isEmpty();
        excludeInactive = isIncludeExcludeInactive(excludeInactive);
        boolean containsInactive = instructionsContains == null || instructionsContains.isBlank();
        boolean notContainsInactive = instructionsNotContains == null || instructionsNotContains.isBlank();

        return vegetarianInactive
                && servingsInactive
                && includeInactive
                && excludeInactive
                && containsInactive
                && notContainsInactive;
    }

    private boolean isIncludeExcludeInactive(boolean includeExclude) {
        if (!includeExclude && include.stream().anyMatch(String::isBlank)) {
            includeExclude = true;
        }
        return includeExclude;
    }

}