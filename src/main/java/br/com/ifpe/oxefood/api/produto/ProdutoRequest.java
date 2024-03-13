package br.com.ifpe.oxefood.api.produto;


import br.com.ifpe.oxefood.modelo.produto.Produto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoRequest {

    private String titulo;

    private int codigoProduto;
 
    private String descricao;
 
    private int valorUnitario;
 
    private int tempoEntrega;
 
    private int tempoMaximo;

   public Produto build() {

       return Produto.builder()
           .titulo(titulo)
           .codigoProduto(codigoProduto)
           .descricao(descricao)
           .valorUnitario(valorUnitario)
           .tempoEntrega(tempoEntrega)
           .tempoMaximo(tempoMaximo)
           .build();
   }

}
