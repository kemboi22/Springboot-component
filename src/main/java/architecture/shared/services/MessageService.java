package architecture.shared.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    public String get(final String message, @Nullable Object... args) {
        var source = new ResourceBundleMessageSource();

        source.setBasename("messages");

        return StringUtils.capitalize(source.getMessage(message, args, LocaleContextHolder.getLocale()));
    }
}
