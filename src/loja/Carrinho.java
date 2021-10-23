package loja;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Carrinho {
	public static void main(String[] args) {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

		Produto p1 = new Produto(1, "Óleo           ", 50, 3.98);
		Produto p2 = new Produto(2, "Chocolate      ", 50, 4.98);
		Produto p3 = new Produto(3, "Morangos       ", 50, 6.98);
		Produto p4 = new Produto(4, "Danoninho      ", 50, 5.98);
		Produto p5 = new Produto(5, "Requeijão      ", 50, 2.98);
		Produto p6 = new Produto(6, "Alface         ", 50, 1.98);
		Produto p7 = new Produto(7, "Laranja        ", 50, 3.98);
		Produto p8 = new Produto(8, "Desodorante    ", 30, 4.98);
		Produto p9 = new Produto(9, "Papel Higiênico", 50, 11.98);
		Produto p10 = new Produto(10, "Picanha       ", 50, 59.98);

		ArrayList<Produto> produtos = new ArrayList<Produto>();
		ArrayList<Produto> carrinho = new ArrayList<Produto>();

		produtos.add(p1);
		produtos.add(p2);
		produtos.add(p3);
		produtos.add(p4);
		produtos.add(p5);
		produtos.add(p6);
		produtos.add(p7);
		produtos.add(p8);
		produtos.add(p9);
		produtos.add(p10);

		Scanner entrada = new Scanner(System.in);
		int pedido;
		int quantidade;
		char resposta = 'S';
		double total = 0;
		int formaPagamento;
		final double imposto = 0.09;

		System.out.println("Olá, cliente. Informe o seu nome: ");
		String nomeCliente = entrada.next();

		do {
			mostrarProdutos(produtos);
			System.out.println("Olá, " + nomeCliente + "! Seja bem vindo!");

			do {
				System.out.print("Informe o código do produto para compra: \n");
				pedido = entrada.nextInt() - 1;
			} while (pedido < 0 || pedido > 9);

			System.out.print("Informe a quantidade que deseja comprar: \n");
			quantidade = entrada.nextInt();

			if (produtos.get(pedido).getQuantidade() - quantidade >= 0) {
				produtos.get(pedido).setQuantidade(produtos.get(pedido).getQuantidade() - quantidade);
				carrinho.add(new Produto((pedido+1), produtos.get(pedido).getNomeProduto(), quantidade,
						produtos.get(pedido).getPrecounit()));
			} else {
				System.out.println("Estoque indisponível.");
			}

			System.out.print("Deseja continuar comprando? [S/N] \n");
			resposta = entrada.next().charAt(0);
		} while (resposta == 'S' || resposta == 's');

		// alterarQuantidade(carrinho, produtos);

		total = mostrarCarrinho(carrinho);
		System.out.println("Remover itens do carrinho? [S/N]");
		resposta = entrada.next().charAt(0);
		while ((resposta == 'S' || resposta == 's')) {
			remover(carrinho);
			System.out.println("Deseja continuar removendo? [S/N]");
			resposta = entrada.next().charAt(0);
			total = mostrarCarrinho(carrinho);
		}

		System.out.println("===================================================================================");
		System.out.println("\t\t\t\tFORMA DE PAGAMENTO");
		System.out.println("===================================================================================");

		do {
			System.out.println("1 - Pix ou Dinheiro (20% de desconto).");
			System.out.println("2 - Cartão à vista (10% de desconto).");
			System.out.println("3 - Cartão parcelado em até 3x.");
			System.out.println("Escolha o código da opção de pagamento: ");
			formaPagamento = entrada.nextInt();

		} while (formaPagamento < 1 || formaPagamento > 3);

		Pagamento pagamento = new Pagamento();

		if (formaPagamento == 3) {
			System.out.print("Em quantas parcelas: \n");
			int parcelas = entrada.nextInt();
			pagamento.setParcelado(total, parcelas);
		} else if (formaPagamento == 2) {
			
			System.out.println("Cartão Pan tem 15% de desconto.");
			System.out.println("Quer usar o seu cartão Pan?");
			
			char r = entrada.next().charAt(0);
			pagamento.setCredito(total, r);
			
		} else {
			
			pagamento.setPixDinheiro(total);
		}
		
		int nf = 0;
		do {
			Random gerado = new Random();
			 nf = gerado.nextInt(999);
		} while (nf < 100);
		
		System.out.println("=============================================================");
		System.out.println("\t\t\tNOTA FISCAL");
		System.out.println("=============================================================");
		System.out.println("RAZÃO SOCIAL: Supermercado PANdeLó");
		System.out.println("CNPJ: 12.456.789/0001-12");
		System.out.println("ENDEREÇO: Rua do Pão de Ló, 123, Beco Diagonal, São Paulo-SP");
		System.out.println(dtf.format(LocalDateTime.now()));
		System.out.println("NF-e: " + nf );
		System.out.println("-------------------------------------------------------------");

		for (Produto p : carrinho)
			System.out.printf("%s \t\t R$ %.2f\n", p.toStringNota(), p.getPrecounit() * p.getQuantidade());

		System.out.println("-------------------------------------------------------------");
		System.out.printf("VALOR DO DESCONTO: R$ %.2f\n", (total - pagamento.getNovoValor()));
		System.out.printf("VALOR FINAL: R$ %.2f\n", pagamento.getNovoValor());
		System.out.printf("VALOR DO IMPOSTO: R$ %.2f\n", total * imposto);
		System.out.println("=============================================================");

	}

	public static boolean remover(ArrayList<Produto> carrinho) {

		double total = 0.0;

		System.out.println("Digite o código do produto a ser removido");
		int i = (new Scanner(System.in)).nextInt();

		for (Produto produto : carrinho) {
			if (produto.getCodigo() == i) {
				Produto pRemovido = produto;
				carrinho.remove(produto);
				System.out.printf("Produto removido do carrinho: %s\n", pRemovido.getNomeProduto());
				mostrarCarrinho(carrinho);
				return true;
			}
		}
		System.out.println("Produto não existe no carrinho!");
		return false;
	}

	public static boolean alterarQuantidade(ArrayList<Produto> carrinhos, ArrayList<Produto> produtos) {
		System.out.println("===================================================================================");
		System.out.println("\t\t\tALTERANDO QUANTIDADE DE PRODUTO");
		System.out.println("===================================================================================");
		System.out.println("COD\t" + "PRODUTO\t" + "  \t\t\tQUANTIDADE");
		for (Produto p : carrinhos)
			System.out.printf("%d\t %s \t\t\t %d\n", p.getCodigo(), p.getNomeProduto(), p.getQuantidade());

		System.out.println("Digite o código do produto a ser alterado");
		int i = (new Scanner(System.in)).nextInt();
		for (Produto produto : carrinhos) {
			if (produto.getCodigo() == i) {
				System.out.printf("Produto: %s", produto.getNomeProduto());
				System.out.printf("Quantidade no carrinho: %d\t", produto.getQuantidade());
				System.out.print("Estoque disponível: " + produtos.get(i).getQuantidade());

				System.out.printf("\nDigite a nova quantidade de %s", produto.getNomeProduto());
				int quantidade = (new Scanner(System.in)).nextInt();

				Produto pAlterado = produto;

//				carrinho.remove(produto);
//				System.out.printf("Produto removido do carrinho: %s", pRemovido.getNomeProduto());
				return true;
			}
		}
		return false;
	}

	public static double mostrarCarrinho(ArrayList<Produto> carrinho) {

		double total = 0.0;
		System.out.println("\n===================================================================================");
		System.out.println("\t\t\t\t\tCARRINHO");
		System.out.println("===================================================================================");

		System.out.println("COD\t" + "\tPRODUTO" + "\t\t\tQUANTIDADE" + "\tPREÇO/UNI" + "\tVALOR TOTAL");
		for (Produto p : carrinho) {
			total += p.getPrecounit() * p.getQuantidade();
			System.out.printf("%s \t R$ %.2f\n", p, p.getPrecounit() * p.getQuantidade());
		}
		System.out.println("===================================================================================");
		System.out.printf("TOTAL: R$ %.2f\n", total);

		return total;
	}
	
	public static void mostrarProdutos(ArrayList<Produto> produtos) {
		System.out.println("\n==================================================================");
		System.out.println("\t\t\tSupermercado PANdeLó");
		System.out.println("==================================================================");

		System.out.println("COD\t" + "\tPRODUTO" + "\t\t\tQUANTIDADE" + "\tPREÇO/UNI");
		for (Produto p : produtos) {
			
			System.out.printf("%s \t\n", p);
		}
		System.out.println("==================================================================");
		
	}

}
