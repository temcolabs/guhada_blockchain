package io.temco.guhada.blockchain.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * Created by Shin Han
 * Since 2019-11-21
 */
@Entity
@Table(name="user_token_history")
@Getter
@ToString
@Builder
public class UserTokenHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private long id;

    @Column(name="user_id")
    private long userId;

    @Column(name="created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name="changed_balance")
    private BigInteger changedBalance;
}
