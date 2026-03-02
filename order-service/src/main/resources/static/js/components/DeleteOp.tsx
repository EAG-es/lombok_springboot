interface DeleteOpProps {
    location: string;
    id: string;
    onSuccess: () => void;
    onError: (error: string) => void;
    lang: string;
}

const DeleteOp: React.FC<DeleteOpProps> = ({ location, id, onSuccess, onError, lang }) => {
    const React = (window as any).React;
    const [loading, setLoading] = React.useState(true);
    const t = (window as any).i18n[lang] || (window as any).i18n.en;

    const csrfToken = document.querySelector<HTMLMetaElement>('meta[name="_csrf"]')?.content;
    const csrfHeaderName = document.querySelector<HTMLMetaElement>("meta[name='_csrf_header']")?.content;

    React.useEffect(() => {
        if (!id || !id.trim()) {
            onError("Order ID is required to delete.");
            setLoading(false);
            return;
        }

        const callEndpoint = async () => {
            try {
                const response = await fetch(`/orders/${id}`, {
                    method: 'DELETE',
                    headers: {
                        'X-Location': location,
                        [csrfHeaderName || 'X-CSRF-TOKEN']: csrfToken || ''
                    }
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

                onSuccess();
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

(window as any).DeleteOp = DeleteOp;
