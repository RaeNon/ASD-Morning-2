package at.tugraz.asd.LANG.Controller;


import at.tugraz.asd.LANG.Languages;
import at.tugraz.asd.LANG.Messages.in.CreateVocabularyMessageIn;
import at.tugraz.asd.LANG.Messages.out.TranslationOut;
import at.tugraz.asd.LANG.Messages.out.VocabularyLanguageOut;
import at.tugraz.asd.LANG.Messages.out.VocabularyOut;
import at.tugraz.asd.LANG.Model.VocabularyModel;
import at.tugraz.asd.LANG.Service.VocabularyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/vocabulary")
public class VocabularyController {

    @Autowired
    VocabularyService service;

    @PostMapping
    public ResponseEntity addVocabulary(@RequestBody CreateVocabularyMessageIn msg){
       service.saveVocabulary(msg);
       return ResponseEntity.ok(null);
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity getAllVocabulary(){
        ArrayList<VocabularyOut> ret = new ArrayList<>();
        List<VocabularyModel> vocab = service.getAllVocabulary();
        vocab.forEach(el->{
            HashMap<Languages, String> translation = new HashMap<>();
            el.getTranslationVocabMapping().forEach(translationModel -> {
                translation.put(translationModel.getLanguage(), translationModel.getVocabulary());
            });
            ret.add(new VocabularyOut(
                    el.getTopic(),
                    el.getVocabulary(),
                    translation
            ));
        });
        return ResponseEntity.ok(ret);
    }

    @GetMapping (path = "{Language}")
    public ResponseEntity getAllVocabularyOfLanguage(@PathVariable("Language") Languages language)
    {
        VocabularyLanguageOut ret = new VocabularyLanguageOut(service.getAllVocabularyOfLanguage(language));
        return ResponseEntity.ok(ret);
    }

    @GetMapping (path = "{Language}/{word}")
    public ResponseEntity getTranslation(@PathVariable("Language") Languages language, @PathVariable("word") String word)
    {
        TranslationOut ret = new TranslationOut(service.getTranslation(language, word));
        return ResponseEntity.ok(ret);
    }
}
