package com.nfinity.ll.testaz.scheduler.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nfinity.ll.testaz.scheduler.domain.model.JobHistoryEntity;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "JobHistory", path = "JobHistory")
public interface IJobHistoryRepository extends JpaRepository<JobHistoryEntity, Long>, QuerydslPredicateExecutor<JobHistoryEntity>  {

    JobHistoryEntity save(JobHistoryEntity job);

    @Query("select j from JobHistoryEntity j where j.jobName = ?1 and j.jobGroup = ?2")
    List<JobHistoryEntity> findByJob(String jobName, String jobGroup);

    @Query("select j from JobHistoryEntity j where j.triggerName = ?1 and j.triggerGroup = ?2")
    List<JobHistoryEntity> findByTrigger(String triggerName, String triggerGroup);

   //  @Query("select j from JobHistoryEntity j")
   //  Page<JobHistoryEntity> findAll(Pageable pageable);


}
