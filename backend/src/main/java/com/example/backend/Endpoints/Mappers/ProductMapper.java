package com.example.backend.Endpoints.Mappers;

import com.example.backend.Endpoints.dto.ProductDto;
import com.example.backend.Endpoints.dto.UserDetailDto;
import com.example.backend.Entity.ApplicationUser;
import com.example.backend.Entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public abstract class ProductMapper {
    public abstract ProductDto entityToProductDto(Product product);

    public abstract Product dtoToEntity(ProductDto productDto);

}
