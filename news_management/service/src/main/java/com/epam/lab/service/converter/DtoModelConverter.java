package com.epam.lab.service.converter;

import com.epam.lab.dto.Dto;
import com.epam.lab.model.Model;

public interface DtoModelConverter<M extends Model, D extends Dto> {
    D toDto (M model);
    M toModel (D dto);
}
