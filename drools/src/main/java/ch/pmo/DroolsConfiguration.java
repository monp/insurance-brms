package ch.pmo;

import lombok.val;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.kie.spring.KModuleBeanFactoryPostProcessor;
import org.kie.spring.annotations.KModuleAnnotationPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.Arrays;

@Configuration
public class DroolsConfiguration {
    
    private static final String RULES_PATH = "rules/";
    
    @Bean
    public KieFileSystem kieFileSystem() throws IOException {
        val kieFileSystem = kieServices().newKieFileSystem();
        Arrays.stream(getRuleFiles())
                .map(file -> RULES_PATH + file.getFilename())
                .map(path -> ResourceFactory.newClassPathResource(path, "UTF-8"))
                .forEach(kieFileSystem::write);
        return kieFileSystem;
    }

    private Resource[] getRuleFiles() throws IOException {
        return new PathMatchingResourcePatternResolver().getResources("classpath*:" + RULES_PATH + "**/*.*");
    }

    KieServices kieServices() {
        return KieServices.Factory.get();
    }
    
    @Bean
    public KieContainer kieContainer() throws IOException {
        val kieRepository = kieServices().getRepository();
        kieRepository.addKieModule(kieRepository::getDefaultReleaseId);
        kieServices().newKieBuilder(kieFileSystem()).buildAll();
        return kieServices().newKieContainer(kieRepository.getDefaultReleaseId());
    }
    
    @Bean
    public KieBase kieBase() throws IOException {
        return kieContainer().getKieBase();
    }

    @Bean
    public KModuleBeanFactoryPostProcessor kieAnnotationPostProcessor() {
        return new KModuleAnnotationPostProcessor();
    }

}