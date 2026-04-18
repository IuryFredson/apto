import { Button } from "../components/ui/Button";

export default function Home() {
  return (
    <div className="grid grid-cols-1 lg:grid-cols-[260px_1fr_280px] gap-6 p-6 max-w-[1440px] mx-auto h-[calc(100vh-64px)]">
      {/* Left Sidebar */}
      <aside className="sidebar-section hidden lg:flex">
        <div className="flex items-center gap-3">
          <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-apto-primary to-blue-400 flex-shrink-0" />
          <div>
            <div className="font-bold text-apto-text-main">Lucas Silva</div>
            <div className="text-[12px] text-apto-text-muted">
              Eng. Software @ USP
            </div>
          </div>
        </div>

        <div>
          <div className="section-title mb-3">Minhas Preferências</div>
          <div className="flex flex-wrap gap-1.5">
            {["Silencioso", "Sem fumantes", "Notívago", "Visitas ok"].map(
              (tag) => (
                <span
                  key={tag}
                  className="px-2.5 py-1 bg-apto-primary-light text-apto-primary rounded-[6px] text-[12px] font-semibold"
                >
                  {tag}
                </span>
              ),
            )}
          </div>
        </div>

        <div>
          <div className="section-title mb-3">Filtros Rápidos</div>
          <div className="flex flex-col gap-2 text-sm text-apto-text-main">
            <label className="flex items-center gap-2 cursor-pointer">
              <input
                type="checkbox"
                defaultChecked
                className="rounded border-apto-border text-apto-primary focus:ring-apto-primary"
              />
              <span>Próximo ao Campus</span>
            </label>
            <label className="flex items-center gap-2 cursor-pointer">
              <input
                type="checkbox"
                className="rounded border-apto-border text-apto-primary focus:ring-apto-primary"
              />
              <span>Até R$ 1.200,00</span>
            </label>
            <label className="flex items-center gap-2 cursor-pointer">
              <input
                type="checkbox"
                defaultChecked
                className="rounded border-apto-border text-apto-primary focus:ring-apto-primary"
              />
              <span>Garagem inclusa</span>
            </label>
          </div>
        </div>

        <Button className="mt-auto w-full py-2.5 bg-apto-primary rounded-[10px] font-semibold">
          Publicar Vaga
        </Button>
      </aside>

      {/* Main Feed */}
      <section className="flex flex-col gap-6 overflow-y-auto pr-2 custom-scrollbar">
        <div className="flex items-center justify-between">
          <h2 className="text-[22px] font-extrabold text-apto-text-main">
            Sugestões da IA para você
          </h2>
          <span className="text-[12px] text-apto-text-muted">
            Ordenado por compatibilidade
          </span>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {[
            {
              id: 1,
              title: "Quarto Suíte - Butantã",
              type: "Vaga em República",
              price: "1.150",
              match: "98%",
              justification:
                "Ambos preferem silêncio após as 22h e dividem rotina de estudos pesada na Poli.",
              img: "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=400",
            },
            {
              id: 2,
              title: "Studio Moderno - Pinheiros",
              type: "Imóvel Inteiro",
              price: "2.400",
              match: "92%",
              justification:
                "Localização estratégica para seu trajeto de ônibus e perfil de locador verificado.",
              img: "https://images.unsplash.com/photo-1598928506311-c55ded91a20c?w=400",
            },
          ].map((item) => (
            <div
              key={item.id}
              className="bg-white rounded-apto-card border border-apto-border overflow-hidden flex flex-col group cursor-pointer hover:shadow-lg transition-shadow"
            >
              <div className="h-40 bg-gray-100 relative overflow-hidden">
                <img
                  src={item.img}
                  alt={item.title}
                  className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
                  referrerPolicy="no-referrer"
                />
                <div className="absolute top-3 right-3 bg-apto-success text-white px-3 py-1.5 rounded-full text-[12px] font-bold shadow-lg shadow-green-500/30">
                  {item.match} Match
                </div>
              </div>
              <div className="p-4 space-y-3">
                <div>
                  <h3 className="text-lg font-bold text-apto-text-main leading-tight">
                    {item.title}
                  </h3>
                  <div className="flex items-center justify-between text-sm mt-1">
                    <span className="text-apto-text-muted">{item.type}</span>
                    <div className="flex items-center gap-1 font-bold text-amber-400">
                      ★ 4.9
                    </div>
                  </div>
                </div>

                <div className="p-3 bg-red-50 rounded-[10px] border-l-4 border-red-500 text-[12px] leading-[1.4] text-red-900">
                  <span className="font-bold">Por que o match?</span>{" "}
                  {item.justification}
                </div>

                <div className="text-lg font-bold text-apto-primary">
                  R$ {item.price} / mês
                </div>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* Right Sidebar */}
      <aside className="sidebar-section hidden xl:flex">
        <div className="section-title">Atividade Recente</div>
        <div className="flex flex-col gap-4">
          <div className="flex gap-3 text-[13px] leading-tight text-apto-text-main">
            <div className="w-2 h-2 rounded-full bg-apto-success shrink-0 mt-1" />
            <div>
              <span className="font-bold">Marina</span> demonstrou interesse na
              sua vaga em 'República das Artes'.
            </div>
          </div>
          <div className="flex gap-3 text-[13px] leading-tight text-apto-text-main">
            <div className="w-2 h-2 rounded-full bg-apto-primary shrink-0 mt-1" />
            <div>
              Sua reputação subiu para <span className="font-bold">4.8</span>{" "}
              após a última avaliação.
            </div>
          </div>
        </div>

        <div className="mt-8">
          <div className="section-title mb-3">Dicas de Segurança</div>
          <div className="p-3 bg-amber-50 rounded-[10px] text-[12px] text-amber-800 leading-relaxed">
            Nunca realize depósitos antes de visitar o local e verificar o
            contrato no campus.
          </div>
        </div>

        <div className="mt-auto text-center text-[10px] text-apto-text-muted">
          Apto v1.0.4 - Sistema de Reputação Ativo
        </div>
      </aside>
    </div>
  );
}
