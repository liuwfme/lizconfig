package cn.liz.lizconfig.server;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class DistributedLocks {

    @Autowired
    private DataSource dataSource;

    private Connection connection;

    @Getter
    private AtomicBoolean locked = new AtomicBoolean(false);

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void init() {
        try {
            connection = dataSource.getConnection();
        } catch (Exception e) {
            log.warn(" ====== getConnection err, e : ", e);
        }
        executor.scheduleWithFixedDelay(this::tryLock, 1000, 5000, TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
                connection.close();
            }
        } catch (SQLException e) {
            log.warn(" ====== close exception, e : ", e);
        }
    }

    public boolean lock() throws SQLException {
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        connection.createStatement().execute("set innodb_lock_wait_timeout=5");
        connection.createStatement().execute("select app from locks where id=1 for update");
        if (locked.get()) {
            log.info(" ====== reenter this dist lock.");
        } else {
            log.info(" ====== get a dist lock.");
        }
        return true;
    }

    private void tryLock() {
        try {
            lock();
            locked.set(true);
        } catch (Exception e) {
            log.warn(" ====== lock failed!");
            locked.set(false);
        }

    }

}
