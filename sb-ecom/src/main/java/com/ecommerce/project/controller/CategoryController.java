package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    // ==========================================
    // GET ALL CATEGORIES
    // ==========================================

    @Tag(
            name = "Category APIs",
            description = "APIs for managing categories"
    )
    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(

            @RequestParam(
                    name = "pageNumber",
                    defaultValue = AppConstants.PAGE_NUMBER,
                    required = false
            )
            Integer pageNumber,

            @RequestParam(
                    name = "pageSize",
                    defaultValue = AppConstants.PAGE_SIZE,
                    required = false
            )
            Integer pageSize,

            @RequestParam(
                    name = "sortBy",
                    defaultValue = AppConstants.SORT_CATEGORIES_BY,
                    required = false
            )
            String sortBy,

            @RequestParam(
                    name = "sortOrder",
                    defaultValue = AppConstants.SORT_DIR,
                    required = false
            )
            String sortOrder) {

        CategoryResponse categoryResponse =
                categoryService.getAllCategories(
                        pageNumber,
                        pageSize,
                        sortBy,
                        sortOrder
                );

        return new ResponseEntity<>(
                categoryResponse,
                HttpStatus.OK
        );
    }


    // ==========================================
    // CREATE CATEGORY - ADMIN
    // ==========================================

    @Tag(
            name = "Category APIs",
            description = "APIs for managing categories"
    )
    @Operation(
            summary = "Create category by admin",
            description = "Admin can create a new category"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Category is created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid Input",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDTO> createCategoryByAdmin(
            @Valid @RequestBody CategoryDTO categoryDTO) {

        CategoryDTO savedCategoryDTO =
                categoryService.createCategory(categoryDTO);

        return new ResponseEntity<>(
                savedCategoryDTO,
                HttpStatus.CREATED
        );
    }




    // ==========================================
    // UPDATE CATEGORY - ADMIN
    // ==========================================

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategoryByAdmin(
            @Valid @RequestBody CategoryDTO categoryDTO,
            @PathVariable Long categoryId) {

        CategoryDTO savedCategoryDTO =
                categoryService.updateCategory(
                        categoryDTO,
                        categoryId
                );

        return new ResponseEntity<>(
                savedCategoryDTO,
                HttpStatus.OK
        );
    }





    // ==========================================
    // DELETE CATEGORY - ADMIN
    // ==========================================

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategoryByAdmin(
            @PathVariable Long categoryId) {

        CategoryDTO deletedCategory =
                categoryService.deleteCategory(categoryId);

        return new ResponseEntity<>(
                deletedCategory,
                HttpStatus.OK
        );
    }



}
