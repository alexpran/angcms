package org.angcms.repository.richcontent;

import org.angcms.model.richcontent.Tag;
import org.giavacms.api.model.Search;
import org.giavacms.api.repository.AbstractRepository;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Named
@Stateless
@LocalBean
public class TagRepository extends AbstractRepository<Tag>
{

   private static final long serialVersionUID = 1L;

   @PersistenceContext
   EntityManager em;

   @Override
   protected EntityManager getEm()
   {
      return em;
   }

   @Override
   public void setEm(EntityManager em)
   {
      this.em = em;
   }

   @Override
   protected String getDefaultOrderBy()
   {
      return "tagName asc";
   }

   /**
    * criteri di default, comuni a tutti, ma specializzabili da ogni EJB tramite overriding
    */

   @Override
   protected void applyRestrictions(Search<Tag> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {

      // ............................................... TAG fields

      // NAME
      if (search.getObj().getTagName() != null
               && search.getObj().getTagName().trim().length() > 0)
      {
         sb.append(separator).append(" upper ( ").append(alias)
                  .append(".tagName ) like :TAGNAME ");
         params.put("TAGNAME", likeParam(search.getObj().getTagName().trim().toUpperCase()));
         separator = " and ";
      }

      // ............................................... CONTENT TYPE fields

      // ACTIVE TYPE
      if (true)
      {
         sb.append(separator).append(alias)
                  .append(".richContent.richContentType.active = :activeContentType ");
         params.put("activeContentType", true);
         separator = " and ";
      }

      // CONTENT TYPE BY NAME
      if (search.getObj().getRichContent() != null && search.getObj().getRichContent().getRichContentType() != null
               && search.getObj().getRichContent().getRichContentType().getName() != null
               && search.getObj().getRichContent().getRichContentType().getName().trim().length() > 0)
      {
         if (search.getObj().getRichContent().getRichContentType().getName().contains(","))
         {
            String[] names = search.getObj().getRichContent().getRichContentType().getName().split(",");
            StringBuffer orBuffer = new StringBuffer();
            String orSeparator = "";
            for (int i = 0; i < names.length; i++)
            {
               if (names[i].trim().length() > 0)
               {
                  orBuffer.append(orSeparator).append(alias)
                           .append(".richContent.richContentType.name = :NAMETYPE" + i + " ");
                  params.put("NAMETYPE" + i, names[i].trim());
                  orSeparator = " or ";
               }
            }
            if (orBuffer.length() > 0)
            {
               sb.append(separator).append(" ( ").append(orBuffer).append(" ) ");
               separator = " and ";
            }
         }
         else
         {
            sb.append(separator).append(alias)
                     .append(".richContent.richContentType.name = :TYPENAME ");
            params.put("TYPENAME", search.getObj().getRichContent().getRichContentType().getName().trim());
            separator = " and ";
         }

      }

      // CONTENT TYPE BY ID
      if (search.getObj().getRichContent() != null && search.getObj().getRichContent().getRichContentType() != null
               && search.getObj().getRichContent().getRichContentType().getId() != null)
      {
         sb.append(separator).append(alias)
                  .append(".richContent.richContentType.id = :TYPEID ");
         params.put("TYPEID", search.getObj().getRichContent().getRichContentType().getId());
         separator = " and ";
      }

      // ............................................... CONTENT fields

      // ACTIVE CONTENT
      if (true)
      {
         sb.append(separator).append(alias)
                  .append(".richContent.active = :activeContent ");
         params.put("activeContent", true);
         separator = " and ";
      }

      // TITLE
      if (search.getObj().getRichContent() != null && search.getObj().getRichContent().getTitle() != null
               && !search.getObj().getRichContent().getTitle().trim().isEmpty())
      {
         boolean likeSearch = likeSearch(likeParam(search.getObj().getRichContent().getTitle().trim().toUpperCase()),
                  alias, separator,
                  sb, params);
         if (likeSearch)
         {
            separator = " and ";
         }
      }

      // RICHCONTENT.id 1
      if (search.getObj().getRichContent() != null
               && search.getObj().getRichContent().getId() != null
               && search.getObj().getRichContent().getId().trim().length() > 0)
      {
         sb.append(separator).append(alias)
                  .append(".richContent.id = :RICHCONTENTID1 ");
         params.put("RICHCONTENTID1", search.getObj().getRichContent().getId().trim());
         separator = " and ";
      }

      // RICHCONTENT.id 2
      if (search.getObj().getRichContentId() != null
               && search.getObj().getRichContentId().trim().length() > 0)
      {
         sb.append(separator).append(alias)
                  .append(".richContent.id = :RICHCONTENTID2 ");
         params.put("RICHCONTENTID2", search.getObj().getRichContentId().trim());
         separator = " and ";
      }

      // TAG
      if (search.getObj().getRichContent() != null && search.getObj().getRichContent().getTag() != null
               && search.getObj().getRichContent().getTag().trim().length() > 0)
      {
         sb.append(separator).append(alias).append(".richContent.id in ( ");
         sb.append(" select distinct rt.richContent.id from ").append(Tag.class.getSimpleName())
                  .append(" rt where rt.tagName = :RICHCONTENTTAGNAME ");
         sb.append(" ) ");
         params.put("RICHCONTENTTAGNAME", search.getObj().getRichContent().getTag().trim());
         separator = " and ";
      }

      // TAG LIKE
      if (search.getObj().getRichContent() != null && search.getObj().getRichContent().getTagList().size() > 0)
      {
         sb.append(separator).append(" ( ");
         for (int i = 0; i < search.getObj().getRichContent().getTagList().size(); i++)
         {
            sb.append(i > 0 ? " or " : "");

            // da provare quale versione piu' efficiente
            boolean usaJoin = false;
            if (usaJoin)
            {
               sb.append(alias).append(".richContent.id in ( ");
               sb.append(" select distinct rt.richContent.id from ").append(Tag.class.getSimpleName())
                        .append(" rt where upper ( rt.tagName ) like :TAGLIKE").append(i).append(" ");
               sb.append(" ) ");
            }
            else
            {
               sb.append(" upper ( ").append(alias).append(".richContent.tags ) like :TAGLIKE").append(i).append(" ");
            }

            params.put("TAGLIKE" + i, likeParam(search.getObj().getRichContent().getTag().trim().toUpperCase()));
         }
         separator = " and ";
      }

   }

   public void set(String richContentId, List<String> tagList, Date date)
   {
      getEm().createQuery("delete from " + Tag.class.getSimpleName() + " t where t.richContentId = :RICHCONTENTID ")
               .setParameter("RICHCONTENTID", richContentId).executeUpdate();
      if (date == null)
      {
         date = new Date();
      }
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      int day = cal.get(Calendar.DAY_OF_MONTH);
      int month = cal.get(Calendar.MONTH) + 1;
      int year = cal.get(Calendar.YEAR);
      for (String tagName : tagList)
      {
         getEm().persist(new Tag(tagName, richContentId, day, month, year));
      }
   }

   protected boolean likeSearch(String likeText, String alias, String separator, StringBuffer sb,
            Map<String, Object> params)
   {
      sb.append(separator).append(" upper ( ").append(alias).append(".richContent.title ) like :title ");
      params.put("title", likeText);
      return true;
   }

   @Override
   protected Tag construct(List<String> fieldNames, List<Object> fieldValues)
   {
      Tag t = new Tag();
      for (int i = 0; i < fieldNames.size(); i++)
      {
         if ("tagName".equals(fieldNames.get(i)))
         {
            t.setTagName((String) fieldValues.get(i));
         }
         else if ("day".equals(fieldNames.get(i)))
         {
            t.setDay((Integer) fieldValues.get(i));
         }
         else if ("month".equals(fieldNames.get(i)))
         {
            t.setMonth((Integer) fieldValues.get(i));
         }
         else if ("year".equals(fieldNames.get(i)))
         {
            t.setYear((Integer) fieldValues.get(i));
         }
      }
      return t;
   }
}
