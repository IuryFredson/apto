import {
  Settings,
  Shield,
  Award,
  Moon,
  Volume2,
  Users2,
  Sparkles,
} from "lucide-react";
import { Button } from "../components/ui/Button";

export default function Profile() {
  const habits = [
    { label: "Ciclo de Sono", value: "Coruja (Noite)", icon: Moon },
    {
      label: "Tolerância a Barulho",
      value: "Baixa (Prefere Silêncio)",
      icon: Volume2,
    },
    { label: "Visitas Externas", value: "Finais de Semana", icon: Users2 },
    { label: "Limpeza", value: "Moderada", icon: Sparkles },
  ];

  return (
    <div className="max-w-4xl mx-auto px-4 py-12 space-y-8 pb-20">
      <div className="flex flex-col md:flex-row gap-8 items-start">
        <div className="w-full md:w-1/3 space-y-6">
          <div className="bg-white p-6 rounded-apto-section border border-apto-border shadow-sm text-center">
            <div className="relative inline-block mb-4">
              <div className="w-24 h-24 bg-apto-primary-light rounded-2xl flex items-center justify-center mx-auto border-4 border-white shadow-lg overflow-hidden">
                <img
                  src="https://picsum.photos/seed/student/200/200"
                  alt="Avatar"
                  referrerPolicy="no-referrer"
                />
              </div>
              <div className="absolute bottom-0 right-0 w-8 h-8 bg-apto-success border-4 border-white rounded-full"></div>
            </div>
            <h2 className="text-xl font-bold text-apto-text-main">
              Eduardo Silva
            </h2>
            <p className="text-sm text-apto-text-muted">
              Engenharia Mecânica - USP
            </p>

            <div className="mt-6 flex items-center justify-center gap-2 px-4 py-2 bg-apto-bg rounded-lg border border-apto-border">
              <Award className="text-amber-500" size={18} />
              <div className="text-left">
                <p className="text-[10px] uppercase font-bold text-apto-text-muted leading-tight">
                  Reputação
                </p>
                <p className="font-bold text-apto-text-main leading-none">
                  4.9 / 5.0
                </p>
              </div>
            </div>

            <Button
              variant="secondary"
              className="w-full mt-6 flex items-center gap-2 font-bold py-2.5"
            >
              <Settings size={16} />
              Editar Perfil
            </Button>
          </div>

          <div className="bg-white p-6 rounded-apto-section border border-apto-border shadow-sm space-y-4">
            <div className="flex items-center gap-2 mb-2 font-bold text-apto-text-main uppercase text-[12px] tracking-wider">
              <Shield size={18} className="text-apto-primary" />
              Verificações
            </div>
            <div className="space-y-3">
              <div className="flex items-center justify-between text-sm">
                <span className="text-apto-text-muted">Documento ID</span>
                <span className="text-apto-success font-bold">Verificado</span>
              </div>
              <div className="flex items-center justify-between text-sm">
                <span className="text-apto-text-muted">
                  Email Universitário
                </span>
                <span className="text-apto-success font-bold">Verificado</span>
              </div>
              <div className="flex items-center justify-between text-sm">
                <span className="text-apto-text-muted">Redes Sociais</span>
                <span className="text-apto-text-muted font-medium italic">
                  Não conectado
                </span>
              </div>
            </div>
          </div>
        </div>

        <div className="flex-1 space-y-6">
          <section className="bg-white p-8 rounded-apto-section border border-apto-border shadow-sm space-y-6">
            <h3 className="text-2xl font-bold text-apto-text-main">
              Hábitos de Convivência
            </h3>
            <p className="text-apto-text-muted leading-relaxed">
              Estas preferências são a base para o nosso sistema de{" "}
              <strong>matchmaking</strong>. Seja honesto para garantir uma boa
              experiência!
            </p>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mt-6">
              {habits.map((habit) => {
                const Icon = habit.icon;
                return (
                  <div
                    key={habit.label}
                    className="p-4 rounded-xl border border-apto-border bg-apto-bg/50 space-y-1"
                  >
                    <div className="flex items-center gap-2 text-apto-primary mb-1">
                      <Icon size={16} />
                      <span className="text-[10px] font-bold uppercase tracking-wider text-apto-text-muted">
                        {habit.label}
                      </span>
                    </div>
                    <p className="font-bold text-apto-text-main">
                      {habit.value}
                    </p>
                  </div>
                );
              })}
            </div>

            <div className="pt-6 border-t border-apto-border space-y-4">
              <h4 className="font-bold text-apto-text-main uppercase text-[12px] tracking-wider">
                Bio / Sobre Mim
              </h4>
              <p className="text-apto-text-muted leading-relaxed italic border-l-4 border-apto-border pl-4">
                "Estudante do 5º semestre, focado em provas agora então evito
                festas durante a semana. Sou organizado com as áreas comuns e
                gosto de cozinhar nos fins de semana."
              </p>
            </div>
          </section>

          <section className="bg-white p-8 rounded-apto-section border border-apto-border shadow-sm space-y-6">
            <h3 className="text-xl font-bold text-apto-text-main">
              Minhas Avaliações
            </h3>
            <div className="space-y-6">
              {[1, 2].map((i) => (
                <div
                  key={i}
                  className="pb-6 border-b border-apto-border last:border-0 last:pb-0"
                >
                  <div className="flex justify-between items-start mb-2">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 rounded-xl bg-gray-100" />
                      <span className="text-sm font-bold text-apto-text-main">
                        Carlos Magno
                      </span>
                    </div>
                    <div className="flex text-amber-400">
                      <Star size={12} fill="currentColor" />
                      <Star size={12} fill="currentColor" />
                      <Star size={12} fill="currentColor" />
                      <Star size={12} fill="currentColor" />
                      <Star size={12} fill="currentColor" />
                    </div>
                  </div>
                  <p className="text-sm text-apto-text-muted leading-relaxed">
                    "Eduardo foi um ótimo colega de república. Super respeitoso
                    com horários e sempre ajudou na organização geral."
                  </p>
                </div>
              ))}
            </div>
          </section>
        </div>
      </div>
    </div>
  );
}

function Star({ size, fill }: { size: number; fill: string }) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width={size}
      height={size}
      viewBox="0 0 24 24"
      fill={fill}
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2" />
    </svg>
  );
}
