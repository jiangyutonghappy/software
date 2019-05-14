package four.kjgz.logistics.repository;

import four.kjgz.logistics.bean.orderr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderrReposity extends JpaRepository<orderr,Integer> {
    public List<orderr> findByOrdernum(String num);
    @Transactional
    public List<orderr> deleteByOrdernum(String num);
}
