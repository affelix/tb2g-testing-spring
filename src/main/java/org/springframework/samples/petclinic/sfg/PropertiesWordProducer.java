package org.springframework.samples.petclinic.sfg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author André Félix
 * @date 08/03/2021 18:11
 */
@Component
@Profile({"externalized", "laurel-properties"})
@Primary
public class PropertiesWordProducer implements WordProducer {

    private String word;

    @Value("${say.word}")
    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String getWord() {
        return word;
    }
}
