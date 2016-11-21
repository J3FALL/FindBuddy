package org.edu.converter;

import org.edu.model.Category;
import org.edu.model.Comment;
import org.edu.model.Meeting;
import org.edu.model.User;
import org.edu.model.dto.CategoryDto;
import org.edu.model.dto.CommentDto;
import org.edu.model.dto.MeetingDto;
import org.edu.model.dto.UserDto;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;

public class Converter {

    private static ModelMapper modelMapper = new ModelMapper();


    private static MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    static {
        mapperFactory.getConverterFactory().registerConverter(new PassThroughConverter(LocalDate.class, LocalDateTime.class));

        mapperFactory.classMap(User.class, UserDto.class)
                .byDefault()
                .customize(
                        new CustomMapper<User, UserDto>() {
                            @Override
                            public void mapAtoB(User user, UserDto userDto, MappingContext context) {
                                Set<MeetingDto> createdMeetings = new HashSet<>();
                                for (Meeting meeting : user.getCreatedMeetings()) {
                                    createdMeetings.add(modelMapper.map(meeting, MeetingDto.class));
                                }
                                userDto.setCreatedMeetings(createdMeetings);
                            }
                        }
                )
                .register();

        mapperFactory.classMap(Meeting.class, MeetingDto.class)
                .byDefault()
                .field("author.id", "authorId")
                .field("author.name", "authorName")
                .register();

        mapperFactory.classMap(Comment.class, CommentDto.class)
                .byDefault()
                .field("author.id", "authorId")
                .field("author.name", "authorName")
                .register();

        mapperFactory.classMap(Category.class, CategoryDto.class)
                .byDefault().register();
    }

    public static <T> List<T> convert(List<?> sources, Class<T> targetType) {
        List<T> targets = new ArrayList<>();
        for (Object object : sources) {
            targets.add(convert(object, targetType));
        }
        return targets;
    }

    public static <T> T convert(Object source, Class<T> targetType) {
        if (source == null)
            return null;
        return mapperFactory.getMapperFacade().map(source, targetType);
    }




}
