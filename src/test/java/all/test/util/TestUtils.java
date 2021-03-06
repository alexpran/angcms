package all.test.util;

import org.angcms.model.banner.Banner;
import org.angcms.model.banner.BannerTypology;
import org.angcms.model.base.attachment.Image;
import org.angcms.model.lang.Language;
import org.angcms.model.lang.LanguageMapping;
import org.angcms.model.richcontent.RichContent;
import org.angcms.model.richcontent.Tag;
import org.angcms.model.richcontent.type.RichContentType;

import java.util.Date;

public class TestUtils
{

   public static final String TARGET_HOST = "http://localhost:8080";

   public static Banner newBanner()
   {
      Image image = new Image();
      image.setName("banner.jpg");
      image.setActive(true);
      image.setDescription("banner di prova");

      BannerTypology bannerTypology = new BannerTypology();
      bannerTypology.setId(1L);
      Banner newBanner = new Banner();
      newBanner.setImage(image);
      newBanner.setBannerTypology(bannerTypology);
      newBanner.setName("" + System.currentTimeMillis());
      newBanner.setActive(true);
      newBanner.setOnline(true);
      return newBanner;
   }

   public static Language newLanguage(String lang)
   {
      Language language = new Language(lang);
      return language;
   }

   public static LanguageMapping newLanguageMapping(String[] langs) throws Exception
   {
      LanguageMapping languageMapping;
      switch (langs.length)
      {
      case 1:
         languageMapping = new LanguageMapping(langs[0] + "_pageId", langs[0] + "_type", langs[1],
                  langs[1] + "_pageId", langs[1] + "_type", langs[1]);
         break;
      case 2:
         languageMapping = new LanguageMapping(langs[0] + "_pageId", langs[0] + "_type", langs[1],
                  langs[1] + "_pageId", langs[1] + "_type", langs[1]
         );
         break;
      case 3:
         languageMapping = new LanguageMapping(langs[0] + "_pageId", langs[0] + "_type", langs[1],
                  langs[1] + "_pageId", langs[1] + "_type", langs[1],
                  langs[2] + "_pageId", langs[2] + "_type", langs[2]
         );
         break;
      case 4:
         languageMapping = new LanguageMapping(langs[0] + "_pageId", langs[0] + "_type", langs[1],
                  langs[1] + "_pageId", langs[1] + "_type", langs[1],
                  langs[2] + "_pageId", langs[2] + "_type", langs[2],
                  langs[3] + "_pageId", langs[3] + "_type", langs[3]
         );
         break;
      case 5:
         languageMapping = new LanguageMapping(langs[0] + "_pageId", langs[0] + "_type", langs[1],
                  langs[1] + "_pageId", langs[1] + "_type", langs[1],
                  langs[2] + "_pageId", langs[2] + "_type", langs[2],
                  langs[3] + "_pageId", langs[3] + "_type", langs[3],
                  langs[4] + "_pageId", langs[4] + "_type", langs[4]
         );
         break;
      case 6:
         languageMapping = new LanguageMapping(langs[0] + "_pageId", langs[0] + "_type", langs[1],
                  langs[1] + "_pageId", langs[1] + "_type", langs[1],
                  langs[2] + "_pageId", langs[2] + "_type", langs[2],
                  langs[3] + "_pageId", langs[3] + "_type", langs[3],
                  langs[4] + "_pageId", langs[4] + "_type", langs[4],
                  langs[5] + "_pageId", langs[5] + "_type", langs[5]
         );
         break;
      default:
         throw new Exception("non ci siamo!");
      }
      return languageMapping;
   }

   public static BannerTypology newBannerTypology()
   {
      BannerTypology bannerTypology = new BannerTypology();
      bannerTypology.setDescription("news");
      bannerTypology.setName("news");
      return bannerTypology;
   }

   public static RichContentType newRichContentType()
   {
      RichContentType richContentType = new RichContentType();
      richContentType.setName("blog");
      richContentType.setActive(true);
      return richContentType;
   }

   public static RichContent newRichContent(String richContentTypeId)
   {
      RichContentType richContentType = newRichContentType();
      richContentType.setId(Long.valueOf(richContentTypeId));

      Image image = new Image();
      image.setName("post.jpg");
      image.setActive(true);
      image.setDescription("post di prova");

      Tag tag = new Tag();
      tag.setTagName("paura");
      tag.setDay(4);
      tag.setMonth(5);
      tag.setYear(2015);

      RichContent richContent = new RichContent();
      richContent.getTags();
      richContent.setRichContentType(richContentType);
      richContent.setActive(true);
      richContent.setAuthor("fiorenzo");
      richContent.setDate(new Date());
      richContent.setContent("<h1>primo post della stagione</h1>");
      richContent.setTitle("primo post");
      richContent.setPreview("primo post della stagione");
      richContent.addImage(image);
      richContent.setTags("primo" + RichContent.TAG_SEPARATOR + "post" + RichContent.TAG_SEPARATOR + "stagione");
      return richContent;
   }

}
