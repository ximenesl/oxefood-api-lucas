package br.com.ifpe.oxefood.modelo.produto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import jakarta.transaction.Transactional;

@Service
public class ProdutoService {

   @Autowired
   private ProdutoRepository repository;

   @Transactional
   public Produto save(Produto produto) {

       produto.setHabilitado(Boolean.TRUE);
       produto.setVersao(1L);
       return repository.save(produto);
   }
    public List<Produto> listarTodos() {
  
        return repository.findAll();
    }

    public Produto obterPorID(Long id) {

        return repository.findById(id).get();
    }

       @Transactional
    public void update(Long id, Produto produtoAlterado) {
 
       Produto produto = repository.findById(id).get();
       produto.setTitulo(produtoAlterado.getTitulo());
       produto.setCodigoProduto(produtoAlterado.getCodigoProduto());
       produto.setDescricao(produtoAlterado.getDescricao());
       produto.setValorUnitario(produtoAlterado.getValorUnitario());
       produto.setTempoEntrega(produtoAlterado.getTempoEntrega());
       produto.setTempoMaximo(produtoAlterado.getTempoMaximo());
         
       produto.setVersao(produto.getVersao() + 1);
       repository.save(produto);
   }
}
