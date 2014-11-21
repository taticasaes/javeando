package org.cafe.game.clientes;

import org.cafe.domain.clientes.Arquiteto;
import org.cafe.domain.clientes.Cliente;
import org.cafe.domain.clientes.Programador;
import org.cafe.domain.financeiro.Pagamento;
import org.cafe.domain.produto.Pedido;
import org.cafe.game.Panetteria;
import org.cafe.game.core.Assets;
import org.cafe.game.core.JavaGraphics;
import org.cafe.game.core.JavaSpriteSheet;

public class InGameCliente {
    private Cliente cliente;
    private float x = 0, y = 150;
    private Panetteria panetteria;
    private boolean indo = true;
    private JavaSpriteSheet spriteSheet;

    static float speed = (20f / 1000f); // 10 pixels por segundo

    static int POSICAO_CAIXA = 500;
    static int POSICAO_DE_VOLTA = 300;

    public InGameCliente(Cliente cliente, Panetteria panetteria) {
        this.cliente = cliente;
        this.panetteria = panetteria;

        atualizarSprite();
    }

    private void atualizarSprite() {
        if (indo) {
            if (cliente instanceof Programador) {
                spriteSheet = Assets.instance().getProgramadorSpriteRight();
            } else if (cliente instanceof Arquiteto) {
                spriteSheet = Assets.instance().getArquitetoSpriteRight();
            } else {
                spriteSheet = Assets.instance().getCoordenadorSpriteRight();
            }
        } else {
            if (cliente instanceof Programador) {
                spriteSheet = Assets.instance().getProgramadorSpriteLeft();
            } else if (cliente instanceof Arquiteto) {
                spriteSheet = Assets.instance().getArquitetoSpriteLeft();
            } else {
                spriteSheet = Assets.instance().getCoordenadorSpriteLeft();
            }
        }
    }

    public void draw(JavaGraphics graphics) {
        graphics.drawImage(spriteSheet.currentFrame(), x, y);
    }

    public void update(long delta) {
        spriteSheet.update(delta);
        if (indo) {
            x += speed * delta;
            if (x >= POSICAO_CAIXA) {
                // Escolhendo o pedido
                Pedido pedido = cliente.escolherPedido(panetteria.getMenu());

                if (pedido == null) {
                    panetteria.logMessage(cliente.getNome() + " nao encontrou nenhum produto disponivel no menu! ");
                } else {
                    Pagamento pagamento = cliente.escolherTipoDePagamento();
                    boolean sucesso = pagamento.processar(cliente, pedido);
                    if (sucesso) {

                    } else {
                        panetteria.logMessage(cliente.getNome() + " nao conseguiu processar o pagamento! ");
                    }
                }

                indo = false;
                atualizarSprite();
            }
        } else {
            if (y <= POSICAO_DE_VOLTA) {
                y += speed * delta;
            } else {
                x -= speed * delta;
            }

            if (x < 0) {
                panetteria.removerCliente(this);
            }
        }
    }
}
