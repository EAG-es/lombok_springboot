interface ProductDTO {
    id: string;
    name: string;
    description: string;
    price: string;
}

interface UpdateOpProps {
    location: string;
    productDTO: ProductDTO;
    onSuccess: () => void;
    onError: (error: string) => void;
    lang: string;
}

const UpdateOp: React.FC<UpdateOpProps> = ({ location, productDTO, onSuccess, onError, lang }) => {
    const [loading, setLoading] = (window as any).React.useState(true);
    const t = (window as any).i18n[lang] || (window as any).i18n.en;

    const csrfToken = document.querySelector<HTMLMetaElement>('meta[name="_csrf"]')?.content;
    const csrfHeader = document.querySelector<HTMLMetaElement>('meta[name="_csrf_header"]')?.content;

    (window as any).React.useEffect(() => {
        // Check if id is empty
        if (!productDTO.id || !String(productDTO.id).trim()) {
            onError("ID cannot be empty.");
            return;
        }

        // Simulate API call
        const callEndpoint = async () => {
            try {
        // Make actual PUT request to updateProduct
        const response = await fetch(`/products/${productDTO.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'X-Location': location,
                [csrfHeader || 'X-CSRF-TOKEN']: csrfToken || ''
            },
            body: JSON.stringify(productDTO)
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({ message: `HTTP ${response.status}: ${response.statusText}` }));
            throw new Error(errorData.message || `HTTP ${response.status}: ${response.statusText}`);
        }

        const result = await response.json();
        onSuccess();
            } catch (error: any) {
                onError(error.message);
            } finally {
                setLoading(false);
            }
        };

        callEndpoint();
    }, [location, productDTO, onSuccess, onError, lang]);

    const getHttpErrorMessage = (code: number, lang: string): string => {
        const messages: { [key: number]: { en: string; es: string } } = {
            400: { en: "Bad Request: The server could not understand the request.", es: "Solicitud Incorrecta: El servidor no pudo entender la solicitud." },
            401: { en: "Unauthorized: Authentication is required.", es: "No Autorizado: Se requiere autenticación." },
            403: { en: "Forbidden: Access is denied.", es: "Prohibido: El acceso está denegado." },
            404: { en: "Not Found: The requested resource was not found.", es: "No Encontrado: El recurso solicitado no fue encontrado." },
            500: { en: "Internal Server Error: Something went wrong on the server.", es: "Error Interno del Servidor: Algo salió mal en el servidor." }
        };
        return messages[code]?.[lang as 'en' | 'es'] || `HTTP ${code}: Unknown error.`;
    };

    if (loading) {
        return (
            <div className="card shadow-lg border-0 mb-5">
                <div className="card-header bg-dark text-white px-4 py-3">
                    <h4 className="mb-0 text-center">Update Operation</h4>
                </div>
                <div className="card-body p-4 text-center">
                    <div className="alert alert-info border-0 shadow-sm">
                        <strong><i className="bi bi-hourglass-split me-2"></i>{t.labelLoading}</strong>
                    </div>
                </div>
            </div>
        );
    }

    return null; // Should not reach here normally
};

(window as any).UpdateOp = UpdateOp;
