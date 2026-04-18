import { Link, useLocation } from "react-router-dom";
import { Home, Search, Compass, User } from "lucide-react";
import { cn } from "../../lib/utils";
import { Button } from "../ui/Button";

export function Navbar() {
  const location = useLocation();

  const navItems = [
    { label: "Início", href: "/", icon: Home },
    { label: "Buscar", href: "/search", icon: Search },
    { label: "Matchmaking", href: "/matches", icon: Compass },
    { label: "Meu Perfil", href: "/profile", icon: User },
  ];

  return (
    <nav className="fixed top-0 w-full h-16 bg-white border-b border-apto-border px-8 flex items-center justify-between z-50">
      <Link
        to="/"
        className="text-2xl font-extrabold tracking-tighter text-apto-primary"
      >
        APTO
      </Link>

      <div className="hidden md:flex flex-1 max-w-md mx-8">
        <div className="w-full h-10 bg-apto-bg border border-apto-border rounded-lg px-4 flex items-center gap-3 text-apto-text-muted text-sm">
          <Search size={18} />
          <span>Procurar por campus, bairro ou preço...</span>
        </div>
      </div>

      <div className="flex items-center gap-6">
        <div className="hidden sm:flex items-center gap-6">
          {navItems.map((item) => {
            const isActive = location.pathname === item.href;
            return (
              <Link
                key={item.href}
                to={item.href}
                className={cn(
                  "text-sm font-medium transition-colors",
                  isActive
                    ? "text-apto-primary"
                    : "text-apto-text-muted hover:text-apto-primary",
                )}
              >
                {item.label}
              </Link>
            );
          })}
        </div>

        <Button
          size="md"
          className="hidden sm:flex px-5 bg-apto-primary rounded-[10px]"
        >
          Publicar Vaga
        </Button>
      </div>
    </nav>
  );
}
