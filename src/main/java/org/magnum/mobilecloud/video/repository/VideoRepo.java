package org.magnum.mobilecloud.video.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
public interface VideoRepo extends CrudRepository<Video,Long> {
//    @Modifying
//    @Query("update Video v set v.likes = (select vs.likes + 1 from Video vs where vs.id = :id)")
//    void likeVideo(@Param("id") long id);
//
//    @Modifying
//    @Query("update Video v set v.likes = (select vs.likes - 1 from Video vs where vs.id = :id)")
//    void unlikeVideo(@Param("id") long id);
    public Collection<Video> findByName(String title);
    public Collection<Video> findByDurationLessThan(Long duration);
}
