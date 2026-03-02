interface OrderDTO {
    id: string;
    orderNumber: string;
    productId: string;
    quantity: string;
    totalPrice: string;
}

interface CreateOpProps {
    location: string;
    orderDTO: OrderDTO;
    onSuccess: (result: any) => void;
    onError: (error: string) => void;
    lang: string;
}

const CreateOp: React.FC<CreateOpProps> = ({ location, orderDTO, onSuccess, onError, lang }) => {
    const React = (window as any).React;
    const [loading, setLoading] = React.useState(true);
    const t = (window as any).i18n[lang] || (window as any).i18n.en;

    const csrfToken = document.querySelector<HTMLMetaElement>('meta[name="_csrf"]')?.content;
    const csrfHeaderName = document.querySelector<HTMLMetaElement>("meta[name='_csrf_header']")?.content;

    React.useEffect(() => {
        const callEndpoint = async () => {
            try {
                // Ensure ID is empty correctly (it is auto-generated)
                const payload = { ...orderDTO };
                if (payload.id && !payload.id.trim()) {
                    delete (payload as any).id;
                }

                const response = await fetch('/orders', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-Location': location,
                        [csrfHeaderName || 'X-CSRF-TOKEN']: csrfToken || ''
                    },
                    body: JSON.stringify(payload)
                });

                if (!response.ok) {
                    const errorText = await response.text();
                    let errorMsg = `HTTP Error ${response.status}: ${response.statusText}`;
                    try {
                        const errorJson = JSON.parse(errorText);
                        errorMsg = errorJson.message || errorMsg;
                    } catch (e) {
                        errorMsg = errorText || errorMsg;
                    }
                    throw new Error(errorMsg);
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
    }, []);

    if (loading) {
        return (
            <div className="card shadow-lg border-0 mb-5 text-center p-4">
                <div className="alert alert-info border-0 shadow-sm">
                    <strong><i className="bi bi-hourglass-split me-2"></i>{t.labelLoading}</strong>
                </div>
            </div>
        );
    }

    return null;
};

(window as any).CreateOp = CreateOp;
