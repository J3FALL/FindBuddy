package org.edu.converter;

import org.modelmapper.ModelMapper;

public class Converter {

    private static ModelMapper modelMapper = new ModelMapper();

    public static <T> T convert(Object source, Class<T> targetType) {
        if (source == null)
            return null;
        return modelMapper.map(source, targetType);
    }
}
