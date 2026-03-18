% -----------------------------------------------------------------------------
% BASE DE CONOCIMIENTO DE AVES DE COSTA RICA
% Estructura: ave(Nombre, Familia, Reino, Filo, Color, Peso(g), Alimento, Habitat).
% -----------------------------------------------------------------------------

% --- HECHOS ---
ave(yiguirro, 'turdidae', 'animalia', 'chordata', pardo, 77, 'frutas, lombrices, insectos', 'valles, jardines, zonas urbanas').
ave('colibri de talamanca', 'trochilidae', 'animalia', 'chordata', verde, 6, 'nectar', 'bosques de montana, jardines').
ave('tucan pico iris', 'ramphastidae', 'animalia', 'chordata', negro, 400, 'frutas, huevos, insectos', 'bosques humedos tropicales').
ave(quetzal, 'trogonidae', 'animalia', 'chordata', verde, 210, 'aguacatillo, frutas, insectos', 'bosques nubosos').
ave('lapa roja', 'psittacidae', 'animalia', 'chordata', rojo, 1000, 'semillas, frutas, nueces', 'bosques tropicales, costas').
ave('pava negra', 'cracidae', 'animalia', 'chordata', negro, 2000, 'frutas, hojas, insectos', 'bosques humedos de montana').
ave('momoto corona azul', 'momotidae', 'animalia', 'chordata', verde, 150, 'insectos, lagartijas, frutas', 'bordes de bosque, cafetales').
ave('reinita mielera', 'thraupidae', 'animalia', 'chordata', azul, 19, 'nectar, frutas, aranas', 'bosques humedos, jardines').
ave(tangara, 'thraupidae', 'animalia', 'chordata', azul, 35, 'frutas, insectos', 'zonas boscosas, jardines').

ave('oropendola de montezuma', 'icteridae', 'animalia', 'chordata', castano, 220, 'frutas, insectos, nectar', 'bordes de bosque, plantaciones').
ave('bienteveo grande', 'tyrannidae', 'animalia', 'chordata', amarillo, 63, 'insectos, peces, frutas', 'areas abiertas, cerca de agua, zonas urbanas').
ave('campanero tricarunculado', 'cotingidae', 'animalia', 'chordata', blanco, 210, 'frutas', 'bosques montanos humedos').
ave(jilguero, 'turdidae', 'animalia', 'chordata', gris, 50, 'bayas, insectos', 'bosques nubosos, sotobosque').
ave('saltarin colilargo', 'pipridae', 'animalia', 'chordata', negro, 21, 'frutas pequenas, insectos', 'bosques secos, bosques humedos').
ave('colibri de cola rufa', 'trochilidae', 'animalia', 'chordata', verde, 5, 'nectar, insectos pequenos', 'areas abiertas, jardines').
ave('lapa verde', 'psittacidae', 'animalia', 'chordata', verde, 1300, 'semillas, frutas', 'bosques humedos de tierras bajas').
ave('garcilla bueyera', 'ardeidae', 'animalia', 'chordata', blanco, 350, 'insectos, saltamontes, ranas', 'pastizales, granjas').
ave('trogon violaceo', 'trogonidae', 'animalia', 'chordata', violeta, 55, 'frutas, insectos', 'bosques, plantaciones').
ave('pajaro sombrilla', 'cotingidae', 'animalia', 'chordata', negro, 450, 'frutas, insectos, lagartijas', 'bosques humedos').
% -----------------------------------------------------------------------------
% REGLAS DE INFERENCIA
% -----------------------------------------------------------------------------

% --- Reglas auxiliares ---
tiene_color(Nombre, Color) :-
    ave(Nombre, _, _, _, Color, _, _, _).

% CORRECCIÓN AQUÍ: ave(...) ahora tiene 8 argumentos
come(Nombre, Comida) :-
    ave(Nombre, _, _, _, _, _, AlimentoCompleto, _),
    sub_atom(AlimentoCompleto, _, _, _, Comida).

% CORRECCIÓN AQUÍ: ave(...) ahora tiene 8 argumentos
vive_en(Nombre, Lugar) :-
    ave(Nombre, _, _, _, _, _, _, HabitatCompleto),
    sub_atom(HabitatCompleto, _, _, _, Lugar).


% --- Reglas de verificación con comodín ---
verificar_color(Nombre, Color) :-
    (Color == cualquiera -> true; tiene_color(Nombre, Color)).

verificar_alimento(Nombre, Comida) :-
    (Comida == cualquiera -> true; come(Nombre, Comida)).

verificar_habitat(Nombre, Lugar) :-
    (Lugar == cualquiera -> true; vive_en(Nombre, Lugar)).


% --- REGLA MAESTRA DE BÚSQUEDA FLEXIBLE ---
buscar_ave_flex(Nombre, Color, Comida, Lugar) :-
    ave(Nombre, _, _, _, _, _, _, _),
    verificar_color(Nombre, Color),
    verificar_alimento(Nombre, Comida),
    verificar_habitat(Nombre, Lugar).