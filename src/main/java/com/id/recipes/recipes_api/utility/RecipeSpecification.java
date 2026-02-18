package com.id.recipes.recipes_api.utility;

import com.id.recipes.recipes_api.model.Ingredient;
import com.id.recipes.recipes_api.model.Recipe;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RecipeSpecification {

        /**
         *  Search mechanism
         * @param criteria received
         * @return Sepecification recipe
         */
        public static Specification<Recipe> search(SearchCriteria criteria) {

        return (root, query, cb) -> {

            // Fetch ingredients to return recipe WITH ingredients
            if (Recipe.class.equals(query.getResultType())) {
                root.fetch("ingredients", JoinType.LEFT);
                query.distinct(true);
            }

            List<Predicate> predicates = new ArrayList<>();

            // Vegetarian filter
            if (criteria.vegetarian() != null) {
                predicates.add(
                        cb.equal(root.get("vegetarian"), criteria.vegetarian())
                );
            }

            // Servings filter
            if (criteria.servings() != null) {
                predicates.add(
                        cb.equal(root.get("servings"), criteria.servings())
                );
            }

            // Description contains
            if (StringUtils.isNotBlank(criteria.instructionsContains())) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("instructions")),
                                "%" + criteria.instructionsContains().toLowerCase() + "%"
                        )
                );
            }

            // Description does NOT contains
            if (StringUtils.isNotBlank(criteria.instructionsNotContains())) {

                predicates.add(
                        cb.notLike(
                                cb.lower(root.get("instructions")),
                                "%" + criteria.instructionsNotContains().toLowerCase() + "%"
                        )
                );
            }

            // INCLUDE ingredients (must contain at least one)
            if (criteria.include() != null &&
                    !criteria.include().isEmpty()) {

                Join<Recipe, Ingredient> join = root.join("ingredients", JoinType.INNER);

                predicates.add(
                        cb.lower(join.get("name"))
                                .in(criteria.include()
                                        .stream()
                                        .map(String::toLowerCase)
                                        .toList())
                );
            }


            // EXCLUDE ingredients
            if (criteria.exclude() != null &&
                    !criteria.exclude().isEmpty()) {

                Join<Recipe, Ingredient> join = root.join("ingredients", JoinType.INNER);

                Subquery<Long> subquery = query.subquery(Long.class);
                Root<Ingredient> subRoot = subquery.from(Ingredient.class);

                subquery.select(subRoot.get("recipeId"))
                        .where(
                                cb.lower(subRoot.get("name"))
                                        .in(criteria.exclude()
                                                .stream()
                                                .map(String::toLowerCase)
                                                .toList())
                        );

                predicates.add(
                        cb.not(root.get("id").in(subquery))
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
