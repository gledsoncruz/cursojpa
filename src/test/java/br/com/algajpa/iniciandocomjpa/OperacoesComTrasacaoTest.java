package br.com.algajpa.iniciandocomjpa;

import br.com.algajpa.model.Produto;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class OperacoesComTrasacaoTest extends EntityManagerTest {

    @Test
    public void MostrarDiferencaPersisMerge() {
        Produto produtoPersist = new Produto();

        produtoPersist.setId(5);
        produtoPersist.setNome("SmartPhone One Plus");
        produtoPersist.setDescricao("O processador mais rapido");
        produtoPersist.setPreco(new BigDecimal(2000));

        entityManager.getTransaction().begin();
        entityManager.persist(produtoPersist);
        produtoPersist.setNome("Smartphone Two Plus");
        entityManager.getTransaction().commit();

        entityManager.clear();

        Produto produtoVerificacao = entityManager.find(Produto.class, produtoPersist.getId());
        Assert.assertNotNull(produtoVerificacao);


        // ------------

        Produto produtoMerge = new Produto();

        produtoMerge.setId(6);
        produtoMerge.setNome("Notebook Dell");
        produtoMerge.setDescricao("O melhor da categoria");
        produtoMerge.setPreco(new BigDecimal(2000));

        entityManager.getTransaction().begin();
        // O merge gera uma cópia de produtoMerge e joga no entityManager, ou seja, o produtoMerge não está sendo gerenciado,
        // para ele ser gerenciado tem que criar um objeto que vai receber o retorno do entityManager.merge
        // Ex. Produto produtoMerge = entityManager.merge(produtoMerge); (dessa forma agora o produtoMerge está sendo gerenciado
        entityManager.merge(produtoMerge);
        // Não faz efeito pq o produtoMerge não está sendo gerenciado
        produtoMerge.setNome("Notebook Dell 2");
        entityManager.getTransaction().commit();

        entityManager.clear();

        Produto produtoVerificacaoMerge = entityManager.find(Produto.class, produtoMerge.getId());
        Assert.assertNotNull(produtoVerificacaoMerge);

    }

    @Test
    public void inserirObjetoComMerge() {
        Produto produto = new Produto();

        produto.setId(4);
        produto.setNome("Microfone Rode");
        produto.setDescricao("A melhor qualidade de som");
        produto.setPreco(new BigDecimal(1000));

        entityManager.getTransaction().begin();
        entityManager.merge(produto);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Produto produtoVerificacao = entityManager.find(Produto.class, produto.getId());
        Assert.assertNotNull(produtoVerificacao);

    }

    @Test
    public void atualizarObjetoGerenciado() {

        Produto produto = entityManager.find(Produto.class, 1);

        entityManager.getTransaction().begin();
        // Já altera no banco pq o objeto é gerenciado
        produto.setNome("Kindle Paper White 2 geracao");
        entityManager.getTransaction().commit();

        Produto produtoVerificacao = entityManager.find(Produto.class, 1);

        Assert.assertEquals("Kindle Paper White 2 geracao", produtoVerificacao.getNome());
    }


    @Test
    public void atualizarObjeto() {

        Produto produto = new Produto();
        produto.setId(1);
        produto.setNome("Kindle Paper White");
        produto.setDescricao("Conheça o novo Kindle");
        produto.setPreco(new BigDecimal(599));

        entityManager.getTransaction().begin();
        entityManager.merge(produto);
        entityManager.getTransaction().commit();

        Produto produtoVerificacao = entityManager.find(Produto.class, 1);

        Assert.assertNotNull(produtoVerificacao);
        Assert.assertEquals("Kindle Paper White", produtoVerificacao.getNome());
    }

    @Test
    public void removerObjeto() {
        Produto produto = entityManager.find(Produto.class, 3);

        entityManager.getTransaction().begin();
        entityManager.remove(produto);
        entityManager.getTransaction().commit();

        Produto produtoVerificacao = entityManager.find(Produto.class, 3);

        Assert.assertNull(produtoVerificacao);
    }

    @Test
    public void inserirPrimeiroObjeto() {
        Produto produto = new Produto();

        produto.setId(2);
        produto.setNome("Câmera Canon");
        produto.setDescricao("A melhor definição para suas fotos");
        produto.setPreco(new BigDecimal(5000));

        entityManager.getTransaction().begin();
        // O persist não precisa estar necessariamente entre a abertura e fechamento de uma transação,
        // pois o entityManager carrega o produto para a memória e fica aguardando uma transacao para de fato commitar
        // na tabela
        entityManager.persist(produto);
        entityManager.getTransaction().commit();

        //Limpa a memória forçando o JPA a fazer um select no banco
        entityManager.clear();

        // Só faz a consulta no banco se houver antes um entityManager.clear(), caso contrario ele já carrega o objeto
        // em memória carregada pelo método persist
        Produto produtoVerificacao = entityManager.find(Produto.class, produto.getId());
        Assert.assertNotNull(produtoVerificacao);

    }

    @Test
    public void abrirEFecharTransacao() {

//        Produto produto = new Produto(); //Somente para o método não mostrar erros

        entityManager.getTransaction().begin();

//        entityManager.persist(produto);
//        entityManager.merge(produto);
//        entityManager.remove(produto);

        entityManager.getTransaction().commit();
    }
}
