package org.woonyong.behavior.compiler;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes("org.woonyong.behavior.annotations.*")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class BehaviorProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // TODO: 어노테이션 처리 및 유효성 검사 또는 코드 생성 로직
        return true;
    }
}
