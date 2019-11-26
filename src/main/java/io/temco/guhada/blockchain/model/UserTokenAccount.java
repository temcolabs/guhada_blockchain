package io.temco.guhada.blockchain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * Created by Shin Han
 * Since 2019-11-21
 */
@Entity
@Table(name="user_token_account")
@NoArgsConstructor
@Getter
@ToString
@Setter
public class UserTokenAccount {
    @Id
    @Column(name="user_id")
    private long userId;

    @Column(name="public_key")
    private String publicKey;

    @Column(name="private_key")
    private String privateKey;

    @Column(name="created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @Column(name="current_balance")
    private BigInteger currentBalance;

    @Column(name="transfer_point_balance")
    private BigInteger transferPointBalance;
}
