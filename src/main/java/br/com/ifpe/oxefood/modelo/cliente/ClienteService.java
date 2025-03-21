package br.com.ifpe.oxefood.modelo.cliente;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifpe.oxefood.modelo.acesso.Usuario;
import br.com.ifpe.oxefood.modelo.acesso.UsuarioService;
import br.com.ifpe.oxefood.modelo.mensagens.EmailService;
import br.com.ifpe.oxefood.util.exception.ClienteException;
import br.com.ifpe.oxefood.util.exception.EntidadeNaoEncontradaException;

@Service
public class ClienteService {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private EnderecoClienteRepository enderecoClienteRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public Cliente save(Cliente cliente, Usuario usuarioLogado) {

        String foneCelular = cliente.getFoneCelular().replaceAll("\\D", "");

        if (cliente.getFoneCelular() == null || !foneCelular.startsWith("81")){
            throw new ClienteException(ClienteException.MSG_NUMERO_INVALIDO);
        }
        usuarioService.save(cliente.getUsuario());

        cliente.setHabilitado(Boolean.TRUE);
        cliente.setVersao(1L);
        cliente.setDataCriacao(LocalDate.now());
        cliente.setCriadoPor(usuarioLogado);

        //emailService.enviarEmailConfirmacaoCadastroCliente(clienteSalvo);
        
        return repository.save(cliente);
}

    public List<Cliente> listarTodos() {

        return repository.findAll();
    }

    public Cliente obterPorID(Long id) {

        Optional<Cliente> consulta = repository.findById(id);

        if (consulta.isPresent()) {
            return consulta.get();
        } else {
            throw new EntidadeNaoEncontradaException("Cliente", id);
        }

    }

    @Transactional
    public void update(Long id, Cliente clienteAlterado, Usuario usuarioLogado) {

        Cliente cliente = repository.findById(id).get();
        cliente.setNome(clienteAlterado.getNome());
        cliente.setDataNascimento(clienteAlterado.getDataNascimento());
        cliente.setCpf(clienteAlterado.getCpf());
        cliente.setFoneCelular(clienteAlterado.getFoneCelular());
        cliente.setFoneFixo(clienteAlterado.getFoneFixo());

        cliente.setVersao(cliente.getVersao() + 1);
        cliente.setDataUltimaModificacao(LocalDate.now());
        cliente.setUltimaModificacaoPor(usuarioLogado);
    

        repository.save(cliente);
    }

    @Transactional
    public void delete(Long id) {

        Cliente cliente = repository.findById(id).get();
        cliente.setHabilitado(Boolean.FALSE);
        cliente.setVersao(cliente.getVersao() + 1);

        repository.save(cliente);
    }

    @Transactional
    public EnderecoCliente adicionarEnderecoCliente(Long clienteId, EnderecoCliente endereco) {
        // alteração na forma de buscar por id (Cliente cliente =
        // this.findById(clienteId);)
        Cliente cliente = repository.findById(clienteId).get();

        // Primeiro salva o EnderecoCliente:

        endereco.setCliente(cliente);
        endereco.setHabilitado(Boolean.TRUE);
        enderecoClienteRepository.save(endereco);

        // Depois acrescenta o endereço criado ao cliente e atualiza o cliente:

        List<EnderecoCliente> listaEnderecoCliente = cliente.getEnderecos();

        if (listaEnderecoCliente == null) {
            listaEnderecoCliente = new ArrayList<EnderecoCliente>();
        }

        listaEnderecoCliente.add(endereco);
        cliente.setEnderecos(listaEnderecoCliente);
        cliente.setVersao(cliente.getVersao() + 1);
        repository.save(cliente);

        return endereco;
    }

    @Transactional
    public EnderecoCliente atualizarEnderecoCliente(Long id, EnderecoCliente enderecoAlterado) {

        EnderecoCliente endereco = enderecoClienteRepository.findById(id).get();
        endereco.setRua(enderecoAlterado.getRua());
        endereco.setNumero(enderecoAlterado.getNumero());
        endereco.setBairro(enderecoAlterado.getBairro());
        endereco.setCep(enderecoAlterado.getCep());
        endereco.setCidade(enderecoAlterado.getCidade());
        endereco.setEstado(enderecoAlterado.getEstado());
        endereco.setComplemento(enderecoAlterado.getComplemento());

        return enderecoClienteRepository.save(endereco);
    }

    @Transactional
    public void removerEnderecoCliente(Long id) {

        EnderecoCliente endereco = enderecoClienteRepository.findById(id).get();
        endereco.setHabilitado(Boolean.FALSE);
        enderecoClienteRepository.save(endereco);

        Cliente cliente = this.obterPorID(endereco.getCliente().getId());
        cliente.getEnderecos().remove(endereco);
        cliente.setVersao(cliente.getVersao() + 1);
        repository.save(cliente);
    }

    @Transactional
    public List<Cliente> filtrar(String nome, String cpf) {

        List<Cliente> listaClientes = repository.findAll();

        if ((nome != null && !"".equals(nome)) &&
                (cpf == null || "".equals(cpf))) {
            listaClientes = repository.findByNome(nome);
        } else if ((nome == null || "".equals(nome)) &&
                (cpf != null && !"".equals(cpf))) {
            listaClientes = repository.findByCpf(cpf);
        } else if ((nome != null && !"".equals(nome)) &&
                (cpf != null && !"".equals(cpf))) {
            listaClientes = repository.consultarNomeeCpf(nome, cpf);
        }
        return listaClientes;

    }

}