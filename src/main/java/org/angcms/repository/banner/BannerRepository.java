package org.angcms.repository.banner;

import org.angcms.model.banner.Banner;
import org.angcms.repository.BaseRepository;
import org.giavacms.api.model.Search;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

import java.util.List;
import java.util.Map;

@Named
@Stateless
@LocalBean
public class BannerRepository extends BaseRepository<Banner>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      // TODO Auto-generated method stub
      return "name asc";
   }

   @Override
   public Banner fetch(Object key)
   {
      try
      {
         Long id;
         if (key instanceof String)
         {
            id = Long.valueOf((String) key);
         }
         else if (key instanceof Long)
         {
            id = (Long) key;
         }
         else
         {
            throw new Exception("key type is not correct!!");
         }
         Banner banner = find(id);
         return banner;
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return null;
      }
   }

   @Override
   protected void applyRestrictions(Search<Banner> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {
      sb.append(separator).append(alias).append(".active = :active");
      params.put("active", true);
      separator = " and ";

      // TYPOLOGY NAME
      if (search.getObj().getBannerTypology() != null
               && search.getObj().getBannerTypology().getName() != null
               && search.getObj().getBannerTypology().getName().trim()
               .length() > 0)
      {
         sb.append(separator).append(alias)
                  .append(".bannerTypology.name = :NAMETYP ");
         params.put("NAMETYP", search.getObj().getBannerTypology().getName());
      }
      // TYPOLOGY ID
      if (search.getObj().getBannerTypology() != null
               && search.getObj().getBannerTypology().getId() != null
               && search.getObj().getBannerTypology().getId() > 0)
      {
         sb.append(separator).append(alias)
                  .append(".bannerTypology.id = :IDTYP ");
         params.put("IDTYP", search.getObj().getBannerTypology().getId());
      }

      // NAME OR DESCRIPTION
      if (search.getObj().getName() != null
               && !search.getObj().getName().isEmpty())
      {
         sb.append(separator + " ( upper(").append(alias)
                  .append(".name) LIKE :NAMEPROD ");
         params.put("NAMEPROD", likeParam(search.getObj().getName()
                  .toUpperCase()));
         sb.append(" or ").append(" upper(").append(alias)
                  .append(".description ) LIKE :DESC").append(") ");
         params.put("DESC", likeParam(search.getObj().getName()
                  .toUpperCase()));
      }
   }

   public Banner getFirst() throws Exception
   {
      List<Banner> list = getList(new Search<Banner>(Banner.class), 0, 1);
      if (list != null && list.size() > 0)
         return list.get(0);
      return null;
   }

   public List<Banner> getRandomByTypology(String typology, int limit) throws Exception
   {
      return getEm()
               .createQuery(
                        "SELECT b FROM Banner b where b.online= :ONLINE AND b.bannerTypology.name = :TIP ORDER BY RAND()")
               .setParameter("TIP", typology).setParameter("ONLINE", true).setMaxResults(limit)
               .getResultList();
   }

}
