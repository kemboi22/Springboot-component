package architecture.shared.services;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

@Service
public class MapperService {
    public <T, U> U map(final T source, Class<U> destinationClass) {
        final var destination = BeanUtils.instantiateClass(destinationClass);

        BeanUtils.copyProperties(source, destination);

        return destination;
    }

    public <T, U> Page<U> map(Page<T> sourcePage, Class<U> destinationClass) {
        final var content = sourcePage.stream().map(source -> map(source, destinationClass)).toList();

        return new PageImpl<>(content, sourcePage.getPageable(), sourcePage.getTotalElements());
    }
}
