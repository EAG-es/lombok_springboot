interface InventoryDTO {
    id: string;
    productId: string;
    quantity: string;
}

interface UpdateOpProps {
    location: string;
    inventoryDTO: InventoryDTO;
    onSuccess: (result: any) => void;
    onError: (error: string) => void;
    lang: string;
}

const UpdateOp: React.FC<UpdateOpProps> = ({ location, inventoryDTO, onSuccess, onError, lang }) => {
    const [loading, setLoading] = (window as any).React.useState(true);
    const t = (window as any).i18n[lang] || (window as any).i18n.en;

    const csrfToken = document.querySelector<HTMLMetaElement>('meta[name="_csrf"]')?.content;
    const csrfHeader = document.querySelector<HTMLMetaElement>('meta[name="_csrf_header"]')?.content;

    (window as any).React.useEffect(() => {
        // Check if id is provided
        if (!inventoryDTO.id || !inventoryDTO.id.trim()) {
            onError(t.labelIdRequired);
            return;
        }

        const callEndpoint = async () => {
            try {
                const response = await fetch(`/inventory/${inventoryDTO.id}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-Location': location,
                        [csrfHeader || 'X-CSRF-TOKEN']: csrfToken || ''
                    },
                    body: JSON.stringify(inventoryDTO)
                });

                if (!response.ok) {
                    const errorData = await response.json().catch(() => ({ message: `HTTP ${response.status}: ${response.statusText}` }));
                    throw new Error(errorData.message || `HTTP ${response.status}: ${response.statusText}`);
                }

                const result = await response.json();
                onSuccess(result);
            } catch (error: any) {
                onError(error.message);
            } finally {
                setLoading(false);
            }
        };

        callEndpoint();
    }, [location, inventoryDTO, onSuccess, onError, lang]);

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

    return null;
};

(window as any).UpdateOp = UpdateOp;
