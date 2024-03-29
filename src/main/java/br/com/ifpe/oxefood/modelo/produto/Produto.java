package br.com.ifpe.oxefood.modelo.produto;

import org.hibernate.annotations.SQLRestriction;

import br.com.ifpe.oxefood.util.entity.EntidadeAuditavel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Produto")
@SQLRestriction("habilitado = true")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Produto extends EntidadeAuditavel  {
    
   @Column
   private String titulo;

   @Column
   private int codigoProduto;

   @Column
   private String descricao;

   @Column
   private double valorUnitario;

   @Column
   private int tempoEntrega;

   @Column
   private int tempoMaximo;

/* 
   Para colocar no Postman
   "titulo": "Pao",
   "codigoProduto":123,
   "descricao":"pao",
   "valorUnitario":1,
   "tempoEntrega":15,
   "tempoMaximo":30
*/
}
